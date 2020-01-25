package com.jahs.context.order.modules.order.domain.bill

import com.jahs.context.order.modules.invoice.domain.InvoiceId
import com.jahs.context.order.modules.order.domain.Order
import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.order.domain.OrderNotFoundException
import com.jahs.context.order.modules.order.domain.create.OrderRepository
import javax.inject.Named

@Named
class OrderBill(private val repository: OrderRepository) {
    operator fun invoke(id: OrderId,
                        invoiceId: InvoiceId) {
        repository
                .search(id)
                .also { assertOrderExists(id, it) }!!
                .bill(invoiceId)
    }


    private fun assertOrderExists(id: OrderId, order: Order?) {
        order ?: throw OrderNotFoundException(id.asString())
    }

}
