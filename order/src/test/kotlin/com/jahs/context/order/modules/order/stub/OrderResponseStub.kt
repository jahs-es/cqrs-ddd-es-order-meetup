package com.jahs.context.order.modules.order.stub

import com.github.javafaker.Faker
import com.jahs.context.order.modules.order.domain.SerializedCartItems
import com.jahs.context.order.modules.order.domain.toSerializedCartItems
import com.jahs.context.order.modules.order.domain.view.OrderResponse
import com.jahs.context.order.modules.user.stub.UserIdStub
import java.math.BigDecimal
import java.time.ZonedDateTime

class OrderResponseStub {
    companion object {
        fun random(orderId: String = OrderIdStub.Companion.random().asString(),
                   createdOn: ZonedDateTime = ZonedDateTime.now(),
                   userId: String = UserIdStub.random().asString(),
                   detailLines: SerializedCartItems = DetalLinesStub.Companion.random().toSerializedCartItems(),
                   billed: Boolean = Faker().bool().bool(),
                   total: BigDecimal= BigDecimal(0))
                = OrderResponse(orderId, createdOn, userId, detailLines, billed, total)
    }
}
