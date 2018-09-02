package com.github.lucasls.kotlinsummitdemo2018.backendreactive.repository

import mu.KotlinLogging.logger
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

private val log = logger { }

@Component
class AntifraudRepository(
    webClientBuilder: WebClient.Builder
) {
    private val webClient = webClientBuilder.build()

    data class CardInfo(
        val bin: String,
        val holderName: String,
        val cpf: String
    )

    data class Result(
        val status: Status
    )

    enum class Status {
        APPROVED, REPROVED, FAILED
    }

    fun validate(paymentAttemptId: String, cardInfo: CardInfo, orderItems: Iterable<Any>): Mono<Result> {
        val request = mapOf<String, Any?>(
            "reference" to paymentAttemptId,
            "cardInfo" to cardInfo,
            "orderItems" to orderItems
        )

        return try {
            return webClient
                .post()
                .uri("http://localhost:3000/antifraud/validate")
                .syncBody(request)
                .retrieve()
                .bodyToMono<Result>()
                .onErrorReturn(Result(Status.FAILED))

        } catch (e: Exception) {
            log.error("Error calling antifraud", e)
            Mono.just(Result(Status.FAILED))
        }
    }

}