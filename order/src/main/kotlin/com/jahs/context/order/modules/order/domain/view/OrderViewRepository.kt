package com.jahs.context.order.modules.order.domain.view

import com.jahs.context.order.modules.order.domain.OrderId

interface OrderViewRepository {
    fun save(entity: OrderView)
    fun search(id: OrderId): OrderView?

}
