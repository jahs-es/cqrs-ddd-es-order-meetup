package com.jahs.context.order.modules.invoice.domain.view.find.by_id

import com.jahs.context.order.modules.invoice.domain.InvoiceId
import com.jahs.context.order.modules.invoice.domain.view.toResponse
import com.kotato.cqrs.domain.query.QueryHandler
import javax.inject.Named

@Named
open class FindInvoiceQueryHandler(private val finder: InvoiceFinder) {

    @QueryHandler
    open fun on(query: FindInvoiceQuery)
            = finder(InvoiceId.fromString(query.id)).toResponse()

}
