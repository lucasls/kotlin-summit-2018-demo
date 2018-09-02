package com.github.lucasls.kotlinsummitdemo2018.backendreactive.repository

import com.github.lucasls.kotlinsummitdemo2018.backendreactive.helper.preparedQuery
import io.reactiverse.pgclient.PgClient
import io.reactiverse.pgclient.Tuple
import io.vertx.core.json.JsonObject
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class RestaurantConfigRepository(
    private val pgClient: PgClient
) {

    data class RestaurantConfig(
        val acquirerKey: String
    )

    fun loadRestaurantConfig(restaurantId: String): Mono<RestaurantConfig?> {

        return pgClient.preparedQuery("select config from restaurant_configs where restaurant_id=$1", Tuple.of(restaurantId))
            .map { rowSet ->
                rowSet.firstOrNull()
                    ?.getJson("config")
                    ?.value()
                    ?.let { it as JsonObject }
                    ?.mapTo(RestaurantConfig::class.java)
            }

    }

}