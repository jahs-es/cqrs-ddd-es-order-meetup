package com.jahs.context.order.modules.order.stub

import com.jahs.context.order.modules.order.domain.bill.OrderBillEvent
import com.jahs.context.order.modules.invoice.stub.InvoiceIdStub
import java.time.ZonedDateTime

class OrderBilledEventStub {
    companion object {
        fun random(aggregateId: String = InvoiceIdStub.Companion.random().asString(),
                   occurredOn: ZonedDateTime = ZonedDateTime.now(),
                   orderId: String = InvoiceIdStub.random().asString()) =
                OrderBillEvent(aggregateId, occurredOn, orderId)
    }
}
