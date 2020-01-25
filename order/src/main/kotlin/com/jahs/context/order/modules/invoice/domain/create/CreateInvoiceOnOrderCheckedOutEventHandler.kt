package com.jahs.context.order.modules.invoice.domain.create

import com.jahs.context.order.modules.order.domain.bill.OrderBillEvent
import com.jahs.context.order.modules.invoice.domain.InvoiceId
import com.jahs.context.order.modules.order.domain.OrderId
import org.axonframework.eventhandling.EventHandler
import javax.inject.Named

@Named
open class CreateInvoiceOnOrderCheckedOutEventHandler(private val creator: InvoiceCreator) {

    @EventHandler
    open fun on(event: OrderBillEvent) {
        creator(InvoiceId.fromString(event.invoiceId),
                OrderId.fromString(event.aggregateId))
    }
}
