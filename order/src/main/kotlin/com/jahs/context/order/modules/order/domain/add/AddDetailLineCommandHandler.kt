package com.jahs.context.order.modules.order.domain.add

import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.order.domain.DetailLine
import com.jahs.context.order.modules.detail_line.domain.ItemId
import com.kotato.cqrs.domain.command.CommandHandler
import com.jahs.shared.money.Money
import javax.inject.Named

@Named
open class AddDetailLineCommandHandler(private val adder: DetailLineAdder) {

    @CommandHandler
    fun on(command: AddDetailLineCommand) {
        adder(OrderId.fromString(command.orderId), command.toDetailLine(), command.quantity)
    }

    private fun AddDetailLineCommand.toDetailLine() =
            DetailLine(itemId = ItemId.fromString(itemId),
                     price = Money.of(price, currency))

}
