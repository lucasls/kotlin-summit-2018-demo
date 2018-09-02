package com.github.lucasls.kotlinsummitdemo2018.backendmvc.helper

import io.reactiverse.pgclient.PgClient
import io.reactiverse.pgclient.PgRowSet
import io.reactiverse.pgclient.Tuple
import java.util.concurrent.CompletableFuture

fun PgClient.preparedQuery(sql: String, arguments: Tuple): PgRowSet {
    val future = CompletableFuture<PgRowSet>()

    this.preparedQuery(sql, arguments) { event ->
        if (event.succeeded()) {
            future.complete(event.result())
        } else {
            future.completeExceptionally(event.cause())
        }
    }

    return future.join()
}