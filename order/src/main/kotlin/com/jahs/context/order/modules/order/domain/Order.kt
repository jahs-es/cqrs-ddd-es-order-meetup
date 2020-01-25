package com.jahs.context.order.modules.order.domain

import com.jahs.context.order.modules.order.domain.add.DetailLineAddedEvent
import com.jahs.context.order.modules.order.domain.bill.OrderBillEvent
import com.jahs.context.order.modules.order.domain.bill.OrderAlreadyBilledException
import com.jahs.context.order.modules.order.domain.bill.OrderIsEmptyException
import com.jahs.context.order.modules.order.domain.create.OrderCreatedEvent
import com.jahs.context.order.modules.order.domain.subtract.DetailLineIsNotInInvoiceException
import com.jahs.context.order.modules.order.domain.subtract.DetailLineSubtractedEvent
import com.jahs.context.order.modules.detail_line.domain.ItemId
import com.jahs.context.order.modules.invoice.domain.InvoiceId
import com.jahs.context.order.modules.user.domain.UserId
import com.jahs.shared.money.Money
import org.axonframework.commandhandling.model.AggregateIdentifier
import org.axonframework.commandhandling.model.AggregateLifecycle.apply
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.spring.stereotype.Aggregate
import java.math.BigDecimal
import java.time.ZonedDateTime

@Aggregate
class Order {

    @AggregateIdentifier
    lateinit var id: OrderId
        private set
    lateinit var userId: UserId
        private set
    var detailLines: DetailLines = mapOf()
        private set
    var billed = false
        private set
    var total = Money.of(BigDecimal(0),"Eur")
        private set


    @EventSourcingHandler
    fun on(event: OrderCreatedEvent) {
        id = event.aggregateId().let { OrderId.fromString(it) }
        userId = event.userId.let { UserId.fromString(it) }
    }

    @EventSourcingHandler
    fun on(event: DetailLineAddedEvent) {
        DetailLine(itemId = ItemId.fromString(event.itemId),
                price = Money.of(event.price, event.currency))
                .let {
                    detailLines = detailLines.add(it, event.quantity)
                }
    }

    @EventSourcingHandler
    fun on(event: DetailLineSubtractedEvent) {
        DetailLine(itemId = ItemId.fromString(event.itemId),
                price = Money.of(event.price, event.currency))
                .let { detailLines = detailLines.subtract(it, event.quantity) }
    }

    @EventSourcingHandler
    fun on(event: OrderBillEvent) {
        billed = true
    }

    fun addItem(detailLine: DetailLine, quantity: Int) {
        apply(DetailLineAddedEvent(aggregateId = id.asString(),
                occurredOn = ZonedDateTime.now(),
                itemId = detailLine.itemId.asString(),
                quantity = quantity,
                price = detailLine.price.amount,
                currency = detailLine.price.currency))
    }

    fun subtractItem(detailLine: DetailLine, quantity: Int) {
        assertItemExistsInOrder(detailLine)
        apply(DetailLineSubtractedEvent(aggregateId = id.asString(),
                occurredOn = ZonedDateTime.now(),
                itemId = detailLine.itemId.asString(),
                quantity = if (detailLines[detailLine]!!.amount < quantity) detailLines[detailLine]!!.amount else quantity,
                price = detailLine.price.amount,
                currency = detailLine.price.currency))
    }

    fun bill(invoiceId: InvoiceId) {
        assertThereAreItems()
        assertOrderIsNotBilled()
        apply(OrderBillEvent(aggregateId = id.asString(),
                occurredOn = ZonedDateTime.now(),
                invoiceId = invoiceId.asString()))
    }

    private fun assertThereAreItems() {
        if (detailLines.isEmpty()) throw OrderIsEmptyException()
    }

    private fun assertOrderIsNotBilled() {
        if (billed) throw OrderAlreadyBilledException()
    }

    private fun assertItemExistsInOrder(detailLine: DetailLine) {
        if (!detailLines.keys.contains(detailLine)) throw DetailLineIsNotInInvoiceException()
    }

    companion object {
        fun create(id: OrderId, userId: UserId) {
            apply(OrderCreatedEvent(aggregateId = id.asString(),
                    occurredOn = ZonedDateTime.now(),
                    userId = userId.asString()))
        }

    }
}
