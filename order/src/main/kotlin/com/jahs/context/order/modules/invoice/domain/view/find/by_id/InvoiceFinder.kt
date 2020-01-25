package com.jahs.context.order.modules.invoice.domain.view.find.by_id

import com.jahs.context.order.modules.invoice.domain.InvoiceId
import com.jahs.context.order.modules.invoice.domain.view.InvoiceView
import com.jahs.context.order.modules.invoice.domain.view.InvoiceViewNotFoundException
import com.jahs.context.order.modules.invoice.domain.view.InvoiceViewRepository
import javax.inject.Named

@Named
open class InvoiceFinder(private val repository: InvoiceViewRepository) {

    operator fun invoke(id: InvoiceId) =
            repository.search(id).also { assertOrderExists(id, it) }!!

    private fun assertOrderExists(id: InvoiceId, invoice: InvoiceView?) {
        invoice ?: throw InvoiceViewNotFoundException(id.asString())
    }

}
