package com.github.lucasls.kotlinsummitdemo2018.backendcoroutines.domain

data class CardInfo(
    val number: String,
    val holderName: String,
    val cvc: String,
    val expDate: String,
    val cpf: String
)