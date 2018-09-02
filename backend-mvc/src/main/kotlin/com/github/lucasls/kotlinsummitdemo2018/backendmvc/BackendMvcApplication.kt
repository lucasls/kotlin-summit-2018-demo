package com.github.lucasls.kotlinsummitdemo2018.backendmvc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BackendMvcApplication

fun main(args: Array<String>) {
    runApplication<BackendMvcApplication>(*args)
}
