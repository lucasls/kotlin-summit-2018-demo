package com.github.lucasls.kotlinsummitdemo2018.backendmvc.repository

import com.github.lucasls.kotlinsummitdemo2018.backendmvc.domain.CardInfo
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Component
import org.springframework.web.client.postForObject

@Component
class GatewayRepository(
    restTemplateBuilder: RestTemplateBuilder
) {

    private val restTemplate = restTemplateBuilder.build()

    data class Result(
        val status: Status,
        val tid: String?
    )

    enum class Status {
        AUTHORIZED, DECLINED, FAILED
    }

    fun authorize(paymentAttemptId: String, cardInfo: CardInfo): Result {
        val request = mapOf<String, Any?>(
            "reference" to paymentAttemptId,
            "card" to cardInfo
        )

        return restTemplate.postForObject("http://localhost:3000/gateway/authorize", request)!!
    }

}