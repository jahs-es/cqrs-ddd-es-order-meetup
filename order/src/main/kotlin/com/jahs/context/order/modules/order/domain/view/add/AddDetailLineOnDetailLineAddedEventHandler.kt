package com.jahs.context.order.modules.order.domain.view.add

import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.order.domain.DetailLine
import com.jahs.context.order.modules.order.domain.add.DetailLineAddedEvent
import com.jahs.context.order.modules.detail_line.domain.ItemId
import com.jahs.shared.money.Money
import org.axonframework.eventhandling.EventHandler
import javax.inject.Named

@Named
open class AddDetailLineOnDetailLineAddedEventHandler(private val adder: OrderViewDetailLineAdder) {

    @EventHandler
    fun on(event: DetailLineAddedEvent) {
        adder(OrderId.fromString(event.aggregateId()), event.toCartItem(), event.quantity)
    }

    private fun DetailLineAddedEvent.toCartItem() =
            DetailLine(itemId = ItemId.fromString(itemId),
                     price = Money.of(price, currency))
}
