package com.github.lucasls.kotlinsummitdemo2018.backendreactive.repository

import com.github.lucasls.kotlinsummitdemo2018.backendreactive.domain.CardInfo
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

@Component
class GatewayRepository(
    webClientBuilder: WebClient.Builder
) {
    private val webClient = webClientBuilder.build()

    data class Result(
        val status: Status,
        val tid: String?
    )

    enum class Status {
        AUTHORIZED, DECLINED, FAILED
    }

    fun authorize(paymentAttemptId: String, cardInfo: CardInfo): Mono<Result> {
        val request = mapOf<String, Any?>(
            "reference" to paymentAttemptId,
            "card" to cardInfo
        )

        return webClient
            .post()
            .uri("http://localhost:3000/gateway/authorize")
            .syncBody(request)
            .retrieve()
            .bodyToMono()
    }

}