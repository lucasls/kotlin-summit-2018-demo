package com.github.lucasls.kotlinsummitdemo2018.backendmvc.domain

import com.github.lucasls.kotlinsummitdemo2018.backendmvc.repository.AntifraudRepository
import com.github.lucasls.kotlinsummitdemo2018.backendmvc.repository.GatewayRepository
import com.github.lucasls.kotlinsummitdemo2018.backendmvc.repository.PaymentAttemptRepository
import com.github.lucasls.kotlinsummitdemo2018.backendmvc.repository.RestaurantConfigRepository
import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.util.*

private val log = KotlinLogging.logger {  }

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

    fun createPaymentAttempt(valueCents: Int, restaurantId: String, cardInfo: CardInfo, orderItems: Iterable<Any>): PaymentAttempt {
        val paymentAttemptId = UUID.randomUUID().toString()

        var antifraudResult: AntifraudRepository.Result? = null
        var gatewayResult: GatewayRepository.Result? = null
        var status: PaymentAttempt.Status = PaymentAttempt.Status.PAID

        block@ try {

            val restaurantConfig = restaurantConfigRepository.loadRestaurantConfig(restaurantId)

            if (restaurantConfig == null) {
                log.warn("No config found for restaurant $restaurantId")
                status = PaymentAttempt.Status.FAILED
                return@block
            }

            antifraudResult = antifraudRepository.validate(paymentAttemptId, cardInfo.forAntifraud(), orderItems)

            if (antifraudResult.status == AntifraudRepository.Status.REPROVED) {
                status = PaymentAttempt.Status.DECLINED
                return@block
            }

            gatewayResult = gatewayRepository.authorize(paymentAttemptId, cardInfo)

            status = when(gatewayResult.status) {
                GatewayRepository.Status.AUTHORIZED -> PaymentAttempt.Status.PAID
                GatewayRepository.Status.DECLINED -> PaymentAttempt.Status.DECLINED
                GatewayRepository.Status.FAILED -> PaymentAttempt.Status.FAILED
            }

        } catch (e: Exception) {
            log.error("An unexpected error occurred", e)
            status = PaymentAttempt.Status.FAILED

        } finally {
            val paymentAttempt = PaymentAttempt(
                id = paymentAttemptId,
                restaurantId = restaurantId,
                valueCents = valueCents,
                antifraudResult = antifraudResult,
                gatewayResult = gatewayResult,
                status = status
            )

            paymentAttemptRepository.insertPaymentAttempt(paymentAttempt)

            return paymentAttempt
        }
    }
}