package com.github.lucasls.kotlinsummitdemo2018.backendreactive

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BackendReactiveApplication

fun main(args: Array<String>) {
    runApplication<BackendReactiveApplication>(*args)
}
