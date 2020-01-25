package com.jahs.context.order.modules.invoice.adapter.find

import com.jahs.context.order.modules.invoice.domain.view.InvoiceResponse
import com.jahs.context.order.modules.invoice.domain.view.find.by_id.FindInvoiceQuery
import com.kotato.cqrs.domain.query.QueryBus
import com.kotato.cqrs.domain.query.ask
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import javax.inject.Inject

@RestController
open class FindInvoiceController(@Inject val queryBus: QueryBus) {

    @GetMapping("/order/invoice/{invoiceId}")
    open fun find(@PathVariable("invoiceId") id: String) =
            queryBus.ask<InvoiceResponse>(FindInvoiceQuery(id))
                    .toRestResponse()
                    .let { ResponseEntity.ok(it) }

}
