package com.jahs.context.order.modules.invoice.domain.create

import com.jahs.shared.domainevent.DomainEvent
import java.math.BigDecimal
import java.time.ZonedDateTime


data class InvoiceCreatedEvent(val aggregateId: String,
                               val occurredOn: ZonedDateTime,
                               val orderId: String,
                               val userId: String,
                               val price: BigDecimal,
                               val currency: String) : DomainEvent {

    override fun aggregateId() = aggregateId
    override fun occurredOn() = occurredOn

}
