package com.jahs.context.order.modules.invoice.domain.view.create

import com.jahs.context.order.modules.invoice.domain.InvoiceId
import com.jahs.context.order.modules.invoice.domain.create.InvoiceCreatedEvent
import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.user.domain.UserId
import com.jahs.shared.money.Money
import org.axonframework.eventhandling.EventHandler
import javax.inject.Named

@Named
open class CreateInvoiceViewOnInvoiceCreatedEventHandler(private val creator: InvoiceViewCreator) {

    @EventHandler
    open fun on(event: InvoiceCreatedEvent) {
        creator(InvoiceId.fromString(event.aggregateId),
                event.occurredOn,
                OrderId.fromString(event.orderId),
                UserId.fromString(event.userId),
                Money.of(event.price, event.currency))
    }
}
