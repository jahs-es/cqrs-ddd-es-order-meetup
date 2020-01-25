package com.jahs.context.order.modules.order.stub

import com.jahs.context.order.modules.order.domain.bill.BillOrderCommand
import com.jahs.context.order.modules.invoice.stub.InvoiceIdStub

class BillOrderCommandStub {
    companion object {
        fun random(id: String = InvoiceIdStub.Companion.random().asString(),
                   orderId: String = InvoiceIdStub.random().asString()) = BillOrderCommand(id, orderId)
    }
}
