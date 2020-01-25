package com.jahs.context.order.modules.invoice.domain.view

import com.jahs.context.order.modules.order.domain.SerializedCartItems
import java.time.ZonedDateTime

data class InvoiceResponse(val id: String,
                           val createdOn: ZonedDateTime,
                           val userId: String,
                           val orderId: String,
                           val status: String,
                           val detailLines: SerializedCartItems)
