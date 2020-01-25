package com.jahs.context.order.modules.invoice.domain.create

import com.jahs.context.order.modules.invoice.domain.InvoiceId
import com.jahs.context.order.modules.order.domain.toCartItems
import com.jahs.context.order.modules.order.domain.view.find.FindOrderQueryAsker
import com.jahs.context.order.modules.invoice.domain.InvoiceRepository
import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.user.domain.UserId
import java.util.UUID
import javax.inject.Named

@Named
open class InvoiceCreator(private val repository: InvoiceRepository,
                          private val asker: FindOrderQueryAsker) {

    operator fun invoke(invoiceId: InvoiceId, orderId: OrderId) {
        asker.ask(orderId).let {
            repository.new(invoiceId,
                           orderId,
                           UserId.fromString(it.userId),
                           it.detailLines.toCartItems())
        }
    }
}
