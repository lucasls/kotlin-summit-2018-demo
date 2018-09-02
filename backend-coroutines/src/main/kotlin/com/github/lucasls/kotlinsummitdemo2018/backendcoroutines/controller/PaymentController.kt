package com.github.lucasls.kotlinsummitdemo2018.backendcoroutines.controller

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.treeToValue
import com.github.lucasls.kotlinsummitdemo2018.backendcoroutines.domain.PaymentService
import kotlinx.coroutines.experimental.reactor.mono
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class PaymentController(
    private val paymentService: PaymentService,
    private val objectMapper: ObjectMapper
) {

    @PostMapping("/payment-attempts/")
    fun createPaymentAttempt(@RequestBody request: JsonNode): Mono<Map<String, Any?>> = mono {
        mapOf(
            "paymenAttempt" to paymentService.createPaymentAttempt(
                valueCents = request["valueCents"].intValue(),
                restaurantId = request["restaurantId"].textValue(),
                cardInfo = objectMapper.treeToValue(request["card"]),
                orderItems = request["orderItems"]
            )
        )
    }

}