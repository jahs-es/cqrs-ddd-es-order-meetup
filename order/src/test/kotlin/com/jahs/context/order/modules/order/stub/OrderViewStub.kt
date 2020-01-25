package com.jahs.context.order.modules.order.stub

import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.order.domain.DetailLines
import com.jahs.context.order.modules.order.domain.view.OrderView
import com.jahs.context.order.modules.user.domain.UserId
import com.jahs.context.order.modules.user.stub.UserIdStub
import java.math.BigDecimal
import java.time.ZonedDateTime

class OrderViewStub {
    companion object {
        fun random(id: OrderId = OrderIdStub.Companion.random(),
                   occurredOn: ZonedDateTime = ZonedDateTime.now(),
                   userId: UserId = UserIdStub.random(),
                   detailLines: DetailLines = emptyMap(),
                   billed: Boolean = false,
                   total: BigDecimal = BigDecimal(0)) =
                OrderView(id, occurredOn, userId, detailLines, billed, total)
    }
}
