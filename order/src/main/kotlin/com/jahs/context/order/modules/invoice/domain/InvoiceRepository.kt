package com.jahs.context.order.modules.invoice.domain

import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.order.domain.DetailLines
import com.jahs.context.order.modules.user.domain.UserId

interface InvoiceRepository {

    fun search(invoiceId: InvoiceId): Invoice?
    fun new(invoiceId: InvoiceId,
            orderId: OrderId,
            userId: UserId,
            detailLines: DetailLines)

}
