package com.github.lucasls.kotlinsummitdemo2018.backendreactive.repository

import com.github.lucasls.kotlinsummitdemo2018.backendreactive.domain.PaymentAttempt
import com.github.lucasls.kotlinsummitdemo2018.backendreactive.helper.preparedQuery
import io.reactiverse.pgclient.PgClient
import io.reactiverse.pgclient.Tuple
import io.reactiverse.pgclient.data.Numeric
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class PaymentAttemptRepository(
    private val pgClient: PgClient
) {

    fun insertPaymentAttempt(paymentAttempt: PaymentAttempt): Mono<Unit> {

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

        return pgClient.preparedQuery(INSERT_QUERY, arguments)
            .map { Unit }
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
