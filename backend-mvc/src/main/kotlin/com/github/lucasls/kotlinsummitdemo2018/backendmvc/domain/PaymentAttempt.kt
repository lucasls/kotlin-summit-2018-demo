package com.github.lucasls.kotlinsummitdemo2018.backendmvc.domain

import com.github.lucasls.kotlinsummitdemo2018.backendmvc.repository.AntifraudRepository
import com.github.lucasls.kotlinsummitdemo2018.backendmvc.repository.GatewayRepository

data class PaymentAttempt(
    val id: String,
    val restaurantId: String,
    val valueCents: Int,
    val antifraudResult: AntifraudRepository.Result?,
    val gatewayResult: GatewayRepository.Result?,
    val status: Status
) {
    enum class Status {
        PAID, DECLINED, FAILED
    }
}