package com.github.lucasls.kotlinsummitdemo2018.backendmvc.repository

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import mu.KotlinLogging
import mu.KotlinLogging.logger
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForObject

private val log = logger { }

@Component
class AntifraudRepository(
    restTemplateBuilder: RestTemplateBuilder
) {

    private val restTemplate = restTemplateBuilder.build()

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

    fun validate(paymentAttemptId: String, cardInfo: CardInfo, orderItems: Iterable<Any>): Result {
        val request = mapOf<String, Any?>(
            "reference" to paymentAttemptId,
            "cardInfo" to cardInfo,
            "orderItems" to orderItems
        )

        return try {
            restTemplate.postForObject("http://localhost:3000/antifraud/validate", request)!!

        } catch (e: Exception) {
            log.error("Error calling antifraud", e)
            Result(Status.FAILED)
        }
    }

}