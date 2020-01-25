package com.jahs.context.order.modules.invoice.infrastructure

import com.jahs.context.order.modules.order.domain.DetailLines
import com.jahs.context.order.modules.invoice.domain.Invoice
import com.jahs.context.order.modules.invoice.domain.InvoiceId
import com.jahs.context.order.modules.invoice.domain.InvoiceRepository
import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.user.domain.UserId
import org.axonframework.commandhandling.model.AggregateNotFoundException
import org.springframework.stereotype.Repository
import org.axonframework.commandhandling.model.Repository as AggregateRepository

@Repository
open class AxonInvoiceRepository(private val persistenceRepository: AggregateRepository<Invoice>) : InvoiceRepository {
    override fun search(invoiceId: InvoiceId) =
        try {
            persistenceRepository.load(invoiceId.asString()).invoke { it }
        } catch (exception: AggregateNotFoundException) {
            null
        }

    override fun new(invoiceId: InvoiceId, orderId: OrderId, userId: UserId, detailLines: DetailLines) {
        persistenceRepository.newInstance {
            Invoice.create(invoiceId, orderId, userId, detailLines)
            Invoice()
        }
    }

}
