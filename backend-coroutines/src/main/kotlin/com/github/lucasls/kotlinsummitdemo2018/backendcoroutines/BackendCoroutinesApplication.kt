package com.github.lucasls.kotlinsummitdemo2018.backendcoroutines

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BackendCoroutinesApplication

fun main(args: Array<String>) {
    runApplication<BackendCoroutinesApplication>(*args)
}
