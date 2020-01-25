package com.jahs.context.order.modules.invoice.domain.view

import com.jahs.context.order.modules.invoice.domain.InvoiceId
import com.jahs.context.order.modules.order.domain.OrderId

interface InvoiceViewRepository {

    fun save(view: InvoiceView)
    fun search(id: InvoiceId): InvoiceView?
    fun searchByOrderId(id: OrderId): InvoiceView?

}
