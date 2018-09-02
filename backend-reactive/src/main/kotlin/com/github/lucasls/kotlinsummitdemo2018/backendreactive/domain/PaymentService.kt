package com.github.lucasls.kotlinsummitdemo2018.backendreactive.domain

import com.github.lucasls.kotlinsummitdemo2018.backendreactive.repository.AntifraudRepository
import com.github.lucasls.kotlinsummitdemo2018.backendreactive.repository.GatewayRepository
import com.github.lucasls.kotlinsummitdemo2018.backendreactive.repository.PaymentAttemptRepository
import com.github.lucasls.kotlinsummitdemo2018.backendreactive.repository.RestaurantConfigRepository
import mu.KotlinLogging
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.*

private val log = KotlinLogging.logger { }

@Component
class PaymentService(
    private val gatewayRepository: GatewayRepository,
    private val antifraudRepository: AntifraudRepository,
    private val paymentAttemptRepository: PaymentAttemptRepository,
    private val restaurantConfigRepository: RestaurantConfigRepository
) {

    private fun CardInfo.forAntifraud(): AntifraudRepository.CardInfo {
        return AntifraudRepository.CardInfo(
            bin = number.substring(0, 6),
            holderName = holderName,
            cpf = cpf
        )
    }

    fun createPaymentAttempt(valueCents: Int, restaurantId: String, cardInfo: CardInfo, orderItems: Iterable<Any>): Mono<PaymentAttempt> {
        val paymentAttemptId = UUID.randomUUID().toString()

        data class Result(
            var continueRunning: Boolean = true,
            var status: PaymentAttempt.Status = PaymentAttempt.Status.PAID,
            var antifraudResult: AntifraudRepository.Result? = null,
            var gatewayResult: GatewayRepository.Result? = null
        )

        val result = Result()

        return restaurantConfigRepository.loadRestaurantConfig(restaurantId).flatMap { restaurantConfig ->
            if (restaurantConfig == null) {
                log.warn("No config found for restaurant $restaurantId")
                result.continueRunning = false
                result.status = PaymentAttempt.Status.FAILED
            }

            if (result.continueRunning) {
                antifraudRepository.validate(paymentAttemptId, cardInfo.forAntifraud(), orderItems)
                    .map { result.antifraudResult = it }
            } else {
                Mono.just(Unit)
            }

        }.flatMap { _ ->
            val antifraudResult = result.antifraudResult

            if (antifraudResult != null) {
                if (antifraudResult.status == AntifraudRepository.Status.REPROVED) {
                    result.continueRunning = false
                    result.status = PaymentAttempt.Status.DECLINED
                }
            }

            if (result.continueRunning) {
                gatewayRepository.authorize(paymentAttemptId, cardInfo)
                    .map { result.gatewayResult = it }
            } else {
                Mono.just(Unit)
            }

        }.map { _ ->
            val gatewayResult = result.gatewayResult

            if (gatewayResult != null) {
                result.status = when (gatewayResult.status) {
                    GatewayRepository.Status.AUTHORIZED -> PaymentAttempt.Status.PAID
                    GatewayRepository.Status.DECLINED -> PaymentAttempt.Status.DECLINED
                    GatewayRepository.Status.FAILED -> PaymentAttempt.Status.FAILED
                }
            }

        }.onErrorResume { e ->
            log.error("An unexpected error occurred", e)
            result.status = PaymentAttempt.Status.FAILED
            Mono.just(Unit)

        }.flatMap { _ ->

            val paymentAttempt = PaymentAttempt(
                id = paymentAttemptId,
                restaurantId = restaurantId,
                valueCents = valueCents,
                antifraudResult = result.antifraudResult,
                gatewayResult = result.gatewayResult,
                status = result.status
            )

            paymentAttemptRepository.insertPaymentAttempt(paymentAttempt)
                .map { paymentAttempt }
        }
    }
}