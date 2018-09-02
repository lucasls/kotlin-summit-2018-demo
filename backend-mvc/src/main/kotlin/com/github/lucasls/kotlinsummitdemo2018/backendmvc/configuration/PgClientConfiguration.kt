package com.github.lucasls.kotlinsummitdemo2018.backendmvc.configuration

import io.reactiverse.pgclient.PgClient
import io.reactiverse.pgclient.PgPoolOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PgClientConfiguration {

    @Bean
    fun pgClient(): PgClient {
        val options = PgPoolOptions().apply {
            port = 5432
            host = "localhost"
            database = "postgres"
            user = "postgres"
            password = "postgres"
            maxSize = 20
            connectTimeout = 1000
            idleTimeout = 10000
        }

        return PgClient.pool(options)
    }
}