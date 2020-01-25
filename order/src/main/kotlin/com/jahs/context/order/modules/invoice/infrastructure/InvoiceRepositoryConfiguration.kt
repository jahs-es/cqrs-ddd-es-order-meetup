package com.jahs.context.order.modules.invoice.infrastructure

import com.jahs.context.order.modules.invoice.domain.Invoice
import org.axonframework.commandhandling.model.Repository
import org.axonframework.common.caching.Cache
import org.axonframework.eventsourcing.AggregateFactory
import org.axonframework.eventsourcing.CachingEventSourcingRepository
import org.axonframework.eventsourcing.Snapshotter
import org.axonframework.eventsourcing.eventstore.EventStore
import org.axonframework.spring.eventsourcing.SpringPrototypeAggregateFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class InvoiceRepositoryConfiguration {

    @Bean
    open fun invoiceAggregateFactory(): AggregateFactory<Invoice> =
            SpringPrototypeAggregateFactory<Invoice>().also {
                it.setPrototypeBeanName(Invoice::class.simpleName!!.toLowerCase())
            }

    @Bean
    open fun invoiceRepository(snapshotter: Snapshotter,
                               eventStore: EventStore,
                               cache: Cache): Repository<Invoice> =
            CachingEventSourcingRepository(invoiceAggregateFactory(), eventStore, cache)
}
