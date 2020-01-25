package com.jahs.context.order.modules.order.domain.create

import com.jahs.shared.domainevent.DomainEvent
import java.time.ZonedDateTime

data class OrderCreatedEvent(val aggregateId: String,
                             val occurredOn: ZonedDateTime,
                             val userId: String) : DomainEvent {
    override fun aggregateId(): String = aggregateId
    override fun occurredOn(): ZonedDateTime = occurredOn
}
