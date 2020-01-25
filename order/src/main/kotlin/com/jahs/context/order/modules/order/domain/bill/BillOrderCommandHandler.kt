package com.jahs.context.order.modules.order.domain.bill

import com.jahs.context.order.modules.invoice.domain.InvoiceId
import com.jahs.context.order.modules.order.domain.OrderId
import com.kotato.cqrs.domain.command.CommandHandler
import javax.inject.Named

@Named
open class BillOrderCommandHandler(private val bill: OrderBill) {

    @CommandHandler
    fun on(command: BillOrderCommand) {
        bill(OrderId.fromString(command.orderId),
                 InvoiceId.fromString(command.invoiceId))
    }
}
