package com.jahs.context.order.modules.invoice.infrastructure.view

import com.jahs.context.order.modules.invoice.domain.InvoiceId
import com.jahs.context.order.modules.invoice.domain.view.InvoiceView
import com.jahs.context.order.modules.invoice.domain.view.InvoiceViewRepository
import com.jahs.context.order.modules.order.domain.OrderId
import org.springframework.data.jpa.repository.JpaRepository
import javax.inject.Named

@Named
open class JpaInvoiceViewRepository(private val persistenceRepository: JpaInvoiceViewPersistenceRepository) : InvoiceViewRepository {

    override fun save(view: InvoiceView) {
        view.let(persistenceRepository::saveAndFlush)
    }

    override fun search(id: InvoiceId): InvoiceView? = id.let(persistenceRepository::findOne)

    override fun searchByOrderId(id: OrderId): InvoiceView? = id.let(persistenceRepository::findByOrderId)


    interface JpaInvoiceViewPersistenceRepository : JpaRepository<InvoiceView, InvoiceId> {
        fun findByOrderId(orderId: OrderId): InvoiceView?
    }
}
