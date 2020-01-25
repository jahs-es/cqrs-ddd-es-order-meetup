package com.jahs.context.order.modules.order.domain.view.find

import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.order.domain.view.OrderResponse
import com.kotato.cqrs.domain.query.QueryBus
import com.kotato.cqrs.domain.query.ask
import javax.inject.Named

@Named
open class FindOrderQueryAsker(private val queryBus: QueryBus) {

    fun ask(orderId: OrderId) = queryBus.ask<OrderResponse>(FindOrderQuery(orderId.asString()))

}
