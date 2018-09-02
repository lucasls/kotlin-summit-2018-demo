package com.github.lucasls.kotlinsummitdemo2018.backendcoroutines.helper

import io.reactiverse.pgclient.PgClient
import io.reactiverse.pgclient.PgRowSet
import io.reactiverse.pgclient.Tuple
import kotlinx.coroutines.experimental.CompletableDeferred

suspend fun PgClient.preparedQuery(sql: String, arguments: Tuple): PgRowSet {
    val deferred = CompletableDeferred<PgRowSet>()

    this.preparedQuery(sql, arguments) { event ->
        if (event.succeeded()) {
            deferred.complete(event.result())
        } else {
            deferred.completeExceptionally(event.cause())
        }
    }

    return deferred.await()
}