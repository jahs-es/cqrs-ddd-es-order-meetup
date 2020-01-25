package com.jahs.context.order.modules.invoice.domain

import com.jahs.context.order.modules.invoice.domain.create.InvoiceCreatedEvent
import com.jahs.context.order.modules.order.domain.DetailLines
import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.order.domain.calculateTotal
import com.jahs.context.order.modules.user.domain.UserId
import com.jahs.shared.money.Money
import org.axonframework.commandhandling.model.AggregateIdentifier
import org.axonframework.commandhandling.model.AggregateLifecycle.apply
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.spring.stereotype.Aggregate
import java.time.ZonedDateTime

@Aggregate
class Invoice {

    @AggregateIdentifier
    lateinit var invoiceId: InvoiceId
        private set
    lateinit var orderId: OrderId
        private set
    lateinit var userId: UserId
        private set
    lateinit var price: Money
        private set
    lateinit var invoiceStatus: InvoiceStatus
        private set

    @EventSourcingHandler
    fun on(event: InvoiceCreatedEvent) {
        invoiceId= InvoiceId.fromString(event.aggregateId)
        orderId = OrderId.fromString(event.orderId)
        userId = UserId.fromString(event.userId)
        invoiceStatus = InvoiceStatus.IN_PROGRESS
        price = Money.of(event.price, event.currency)
    }

    companion object {
        fun create(invoiceId: InvoiceId, orderId: OrderId, userId: UserId, detailLines: DetailLines) {
            val price = detailLines.calculateTotal()
            apply(InvoiceCreatedEvent(invoiceId.asString(),
                                    ZonedDateTime.now(),
                                    orderId.asString(),
                                    userId.asString(),
                                    price.amount,
                                    price.currency))
        }
    }

}
