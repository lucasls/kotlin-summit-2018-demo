package com.github.lucasls.kotlinsummitdemo2018.backendmvc.controller

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.treeToValue
import com.github.lucasls.kotlinsummitdemo2018.backendmvc.domain.PaymentAttempt
import com.github.lucasls.kotlinsummitdemo2018.backendmvc.domain.PaymentService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PaymentController(
    private val paymentService: PaymentService,
    private val objectMapper: ObjectMapper
) {

    @PostMapping("/payment-attempts/")
    fun createPaymentAttempt(@RequestBody request: JsonNode): Map<String, Any?> {

        return mapOf(
            "paymenAttempt" to paymentService.createPaymentAttempt(
                valueCents = request["valueCents"].intValue(),
                restaurantId = request["restaurantId"].textValue(),
                cardInfo = objectMapper.treeToValue(request["card"]),
                orderItems = request["orderItems"]
            )
        )
    }

}