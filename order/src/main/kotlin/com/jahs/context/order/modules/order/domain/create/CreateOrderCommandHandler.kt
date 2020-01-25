package com.jahs.context.order.modules.order.domain.create

import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.user.domain.UserId
import com.kotato.cqrs.domain.command.CommandHandler
import javax.inject.Inject
import javax.inject.Named

@Named
open class CreateOrderCommandHandler(@Inject private val creator: OrderCreator) {

    @CommandHandler
    fun on(command: CreateOrderCommand) {
        creator(OrderId.fromString(command.id), UserId.fromString(command.userId))
    }

}
