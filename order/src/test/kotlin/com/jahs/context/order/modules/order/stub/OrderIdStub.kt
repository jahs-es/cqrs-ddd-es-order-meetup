package com.jahs.context.order.modules.order.stub

import com.jahs.context.order.modules.order.domain.OrderId
import java.util.UUID

class OrderIdStub {
    companion object {
        fun random() = OrderId(UUID.randomUUID())
    }
}
