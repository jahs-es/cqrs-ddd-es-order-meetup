package com.jahs.context.order.modules.order.domain.view

import com.jahs.context.order.modules.order.domain.SerializedCartItems
import java.math.BigDecimal
import java.time.ZonedDateTime

data class OrderResponse(val orderId: String,
                         val createdOn: ZonedDateTime,
                         val userId: String,
                         val detailLines: SerializedCartItems,
                         val billed: Boolean,
                         val total: BigDecimal)
