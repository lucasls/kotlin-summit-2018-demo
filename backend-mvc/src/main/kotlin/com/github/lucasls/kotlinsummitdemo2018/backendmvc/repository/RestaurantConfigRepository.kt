package com.github.lucasls.kotlinsummitdemo2018.backendmvc.repository

import com.github.lucasls.kotlinsummitdemo2018.backendmvc.helper.preparedQuery
import io.reactiverse.pgclient.PgClient
import io.reactiverse.pgclient.Tuple
import io.vertx.core.json.JsonObject
import org.springframework.stereotype.Component

@Component
class RestaurantConfigRepository(
    private val pgClient: PgClient
) {

    data class RestaurantConfig(
        val acquirerKey: String
    )

    fun loadRestaurantConfig(restaurantId: String): RestaurantConfig? {
        val rowSet = pgClient.preparedQuery("select config from restaurant_configs where restaurant_id=$1", Tuple.of(restaurantId))

        return rowSet.firstOrNull()
            ?.getJson("config")
            ?.value()
            ?.let { it as JsonObject }
            ?.mapTo(RestaurantConfig::class.java)
    }

}