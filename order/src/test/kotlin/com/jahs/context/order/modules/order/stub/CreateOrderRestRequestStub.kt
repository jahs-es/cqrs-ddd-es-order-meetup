package com.jahs.context.order.modules.order.stub

import com.jahs.context.order.modules.order.adapter.create.CreateOrderRestRequest
import com.jahs.context.order.modules.user.stub.UserIdStub
import java.util.UUID

class CreateOrderRestRequestStub {
    companion object {
        fun random(orderId: UUID = OrderIdStub.Companion.random().id,
                   userId: UUID = UserIdStub.random().id) = CreateOrderRestRequest(orderId, userId)
    }
}
