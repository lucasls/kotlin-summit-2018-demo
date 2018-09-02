package com.github.lucasls.kotlinsummitdemo2018.backendreactive.helper

import io.reactiverse.pgclient.PgClient
import io.reactiverse.pgclient.PgRowSet
import io.reactiverse.pgclient.Tuple
import io.vertx.core.AsyncResult
import reactor.core.publisher.Mono
import java.util.concurrent.CompletableFuture

private fun <T> mono(block: ((AsyncResult<T>) -> Unit) -> Unit): Mono<T> {

    return Mono.create { sink ->
        block { event ->
            if (event.succeeded()) {
                sink.success(event.result())
            } else {
                sink.error(event.cause())
            }
        }
    }
}

fun PgClient.query(sql: String): Mono<PgRowSet> {
    return mono {
        this.query(sql, it)
    }
}

fun PgClient.preparedQuery(sql: String, arguments: Tuple): Mono<PgRowSet> {
    return mono {
        this.preparedQuery(sql, arguments, it)
    }
}