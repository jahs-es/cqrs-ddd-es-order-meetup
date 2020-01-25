package com.jahs.context.order.modules.invoice.domain.view

import com.jahs.context.order.modules.order.domain.toSerializedCartItems

fun InvoiceView.toResponse() =
        InvoiceResponse(id.asString(),
                      createdOn,
                      userId.asString(),
                      orderId.asString(),
                      status.name,
                      detailLines.toSerializedCartItems())
