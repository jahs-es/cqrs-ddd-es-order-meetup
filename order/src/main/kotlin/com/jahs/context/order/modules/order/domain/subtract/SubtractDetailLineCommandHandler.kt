package com.jahs.context.order.modules.order.domain.subtract

import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.order.domain.DetailLine
import com.jahs.context.order.modules.detail_line.domain.ItemId
import com.kotato.cqrs.domain.command.CommandHandler
import com.jahs.shared.money.Money
import javax.inject.Named

@Named
open class SubtractDetailLineCommandHandler(private val subtractor: DetailLineSubtractor) {

    @CommandHandler
    fun on(command: SubtractDetailLineCommand) {
        subtractor(OrderId.fromString(command.id), command.toDetailLine(), command.quantity)
    }

    private fun SubtractDetailLineCommand.toDetailLine() =
            DetailLine(itemId = ItemId.fromString(itemId),
                     price = Money.of(price, currency))


}
