package com.github.lucasls.kotlinsummitdemo2018.backendmvc.repository

import com.github.lucasls.kotlinsummitdemo2018.backendmvc.domain.PaymentAttempt
import com.github.lucasls.kotlinsummitdemo2018.backendmvc.helper.preparedQuery
import io.reactiverse.pgclient.PgClient
import io.reactiverse.pgclient.Tuple
import io.reactiverse.pgclient.data.Numeric
import org.springframework.stereotype.Component

@Component
class PaymentAttemptRepository(
    private val pgClient: PgClient
) {

    fun insertPaymentAttempt(paymentAttempt: PaymentAttempt) {

        val arguments = with (paymentAttempt) {
            Tuple.of(
                id,
                restaurantId,
                Numeric.create(valueCents.toBigDecimal().movePointLeft(2)),
                antifraudResult?.status?.name,
                gatewayResult?.status?.name,
                gatewayResult?.tid,
                status.name
            )
        }

        pgClient.preparedQuery(INSERT_QUERY, arguments)
    }

}

private val INSERT_QUERY = """
    insert into payment_attempts(
        payment_attempt_id,
        restaurant_id,
        payment_value,
        antifraud_result_status,
        gateway_result_status,
        gateway_result_tid,
        payment_status
    ) values(
        $1,
        $2,
        $3,
        $4,
        $5,
        $6,
        $7
    )""".trimIndent()
