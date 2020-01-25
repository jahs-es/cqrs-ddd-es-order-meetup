package com.jahs.context.order.modules.order.domain.view.subtract

import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.order.domain.DetailLine
import com.jahs.context.order.modules.order.domain.subtract.DetailLineSubtractedEvent
import com.jahs.context.order.modules.detail_line.domain.ItemId
import com.jahs.shared.money.Money
import org.axonframework.eventhandling.EventHandler
import javax.inject.Named

@Named
open class SubtractDetailLineOnDetailLineSubtractedEventHandler(private val subtractor: OrderViewDetailLineSubtractor) {

    @EventHandler
    fun on(event: DetailLineSubtractedEvent) {
        subtractor(OrderId.fromString(event.aggregateId()), event.toDetailLine(), event.quantity)
    }

    private fun DetailLineSubtractedEvent.toDetailLine() =
            DetailLine(itemId = ItemId.fromString(itemId),
                     price = Money.of(price, currency))
}
