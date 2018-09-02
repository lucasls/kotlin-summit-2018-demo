package com.github.lucasls.kotlinsummitdemo2018.backendmvc.helper

import io.reactiverse.pgclient.PgClient
import io.reactiverse.pgclient.PgRowSet
import io.reactiverse.pgclient.Tuple
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import java.util.concurrent.CompletableFuture

private fun <T> sync(block: ((AsyncResult<T>) -> Unit) -> Unit): T {
    val futureResult = CompletableFuture<T>()

    block { event ->
        if (event.succeeded()) {
            futureResult.complete(event.result())
        } else {
            futureResult.completeExceptionally(event.cause())
        }
    }

    return futureResult.join()
}

fun PgClient.query(sql: String): PgRowSet {
    return sync {
        this.query(sql, it)
    }
}

fun PgClient.preparedQuery(sql: String, arguments: Tuple): PgRowSet {
    return sync {
        this.preparedQuery(sql, arguments, it)
    }
}