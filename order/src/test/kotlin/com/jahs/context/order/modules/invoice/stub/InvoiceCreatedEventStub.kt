package com.jahs.context.order.modules.invoice.stub

import com.jahs.context.order.modules.order.domain.calculateTotal
import com.jahs.context.order.modules.order.stub.OrderIdStub
import com.jahs.context.order.modules.order.stub.DetalLinesStub
import com.jahs.context.order.modules.invoice.domain.create.InvoiceCreatedEvent
import com.jahs.context.order.modules.user.stub.UserIdStub
import java.math.BigDecimal
import java.time.ZonedDateTime

class InvoiceCreatedEventStub {
    companion object {
        fun random(aggregateId: String = OrderIdStub.Companion.random().asString(),
                   occurredOn: ZonedDateTime = ZonedDateTime.now(),
                   orderId: String = OrderIdStub.random().asString(),
                   userId: String = UserIdStub.random().asString(),
                   price: BigDecimal = DetalLinesStub.random().calculateTotal().amount,
                   currency: String = DetalLinesStub.random().calculateTotal().currency)
                = InvoiceCreatedEvent(aggregateId, occurredOn, orderId, userId, price, currency)
    }
}
