package com.jahs.context.order.modules.order.stub

import com.jahs.context.order.modules.order.domain.create.CreateOrderCommand
import com.jahs.context.order.modules.user.stub.UserIdStub

class CreateOrderCommandStub {
    companion object {
        fun random(id: String = OrderIdStub.Companion.random().asString(),
                   userId: String = UserIdStub.random().asString()) = CreateOrderCommand(id, userId)
    }
}
