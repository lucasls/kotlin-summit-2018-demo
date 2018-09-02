package com.github.lucasls.kotlinsummitdemo2018.backendmvc.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.core.json.Json
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
class JacksonConfiguration(private val objectMapper: ObjectMapper) {

    @PostConstruct
    fun postConstruct() {
        Json.mapper = objectMapper
    }
}