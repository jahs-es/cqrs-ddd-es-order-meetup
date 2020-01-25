package com.jahs.context.order.modules.order.domain.view.create

import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.order.domain.create.OrderCreatedEvent
import com.jahs.context.order.modules.user.domain.UserId
import org.axonframework.eventhandling.EventHandler
import javax.inject.Named

@Named
open class CreateOrderViewOnOrderCreatedEventHandler(private val creator: OrderViewCreator) {

    @EventHandler
    fun on(event: OrderCreatedEvent) {
        creator(OrderId.fromString(event.aggregateId()),
                event.occurredOn(),
                UserId.fromString(event.userId))
    }

}
