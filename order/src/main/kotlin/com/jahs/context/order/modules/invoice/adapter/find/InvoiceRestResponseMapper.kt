package com.jahs.context.order.modules.invoice.adapter.find

import com.jahs.context.order.modules.invoice.domain.view.InvoiceResponse

fun InvoiceResponse.toRestResponse() =
        InvoiceRestResponse(id, createdOn, userId, orderId, status, detailLines)
