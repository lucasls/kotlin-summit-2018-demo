package com.github.lucasls.kotlinsummitdemo2018.backendreactive.helper

import io.reactiverse.pgclient.PgClient
import io.reactiverse.pgclient.PgRowSet
import io.reactiverse.pgclient.Tuple
import io.vertx.core.AsyncResult
import reactor.core.publisher.Mono

fun PgClient.preparedQuery(sql: String, arguments: Tuple): Mono<PgRowSet> {
    return Mono.create { sink ->

        this.preparedQuery(sql, arguments) { event ->
            if (event.succeeded()) {
                sink.success(event.result())
            } else {
                sink.error(event.cause())
            }
        }

    }
}