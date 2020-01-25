package com.jahs.context.order.modules.order.domain.create

import com.jahs.context.order.modules.order.domain.Order
import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.user.domain.UserId

interface OrderRepository {

    fun new(id: OrderId, userId: UserId)
    fun search(id: OrderId): Order?

}
