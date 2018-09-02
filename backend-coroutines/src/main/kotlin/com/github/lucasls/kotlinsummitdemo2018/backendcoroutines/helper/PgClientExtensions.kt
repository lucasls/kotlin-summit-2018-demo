package com.github.lucasls.kotlinsummitdemo2018.backendcoroutines.helper

import io.reactiverse.pgclient.PgClient
import io.reactiverse.pgclient.PgRowSet
import io.reactiverse.pgclient.Tuple
import io.vertx.core.AsyncResult
import kotlinx.coroutines.experimental.CompletableDeferred

private suspend fun <T> await(block: ((AsyncResult<T>) -> Unit) -> Unit): T {
    val deferred = CompletableDeferred<T>()

    block { event ->
        if (event.succeeded()) {
            deferred.complete(event.result())
        } else {
            deferred.completeExceptionally(event.cause())
        }
    }

    return deferred.await()
}

suspend fun PgClient.query(sql: String): PgRowSet {
    return await {
        this.query(sql, it)
    }
}

suspend fun PgClient.preparedQuery(sql: String, arguments: Tuple): PgRowSet {
    return await {
        this.preparedQuery(sql, arguments, it)
    }
}