package com.jahs.context.order.modules.order.stub

import com.jahs.context.order.modules.order.domain.create.OrderCreatedEvent
import com.jahs.context.order.modules.user.stub.UserIdStub
import java.time.ZonedDateTime

class OrderCreatedEventStub {
    companion object {
        fun random(aggregateId: String = OrderIdStub.Companion.random().asString(),
                   occurredOn: ZonedDateTime = ZonedDateTime.now(),
                   userId: String = UserIdStub.random().asString()) =
                OrderCreatedEvent(aggregateId, occurredOn, userId)
    }
}
