package com.jahs.context.order.modules.order.domain.bill

import com.jahs.shared.domainevent.DomainEvent
import java.time.ZonedDateTime


data class OrderBillEvent(val aggregateId: String,
                          val occurredOn: ZonedDateTime,
                          val invoiceId: String) : DomainEvent {
    override fun aggregateId(): String = aggregateId
    override fun occurredOn(): ZonedDateTime = occurredOn
}
