package com.jahs.context.order.modules.order.domain.add

import com.jahs.shared.domainevent.DomainEvent
import java.math.BigDecimal
import java.time.ZonedDateTime


data class DetailLineAddedEvent(val aggregateId: String,
                                val occurredOn: ZonedDateTime,
                                val itemId: String,
                                val quantity: Int,
                                val price: BigDecimal,
                                val currency: String) : DomainEvent {
    override fun aggregateId(): String = aggregateId
    override fun occurredOn(): ZonedDateTime = occurredOn
}
