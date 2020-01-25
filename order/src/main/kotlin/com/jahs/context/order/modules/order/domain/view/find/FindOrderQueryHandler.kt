package com.jahs.context.order.modules.order.domain.view.find

import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.order.domain.view.toResponse
import com.kotato.cqrs.domain.query.QueryHandler
import javax.inject.Named

@Named
open class FindOrderQueryHandler(private val finder: OrderFinder) {

    @QueryHandler
    open fun on(query: FindOrderQuery) = finder(OrderId.fromString(query.id)).toResponse()

}
