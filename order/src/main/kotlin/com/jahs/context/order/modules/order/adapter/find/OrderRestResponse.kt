package com.jahs.context.order.modules.order.adapter.find

import com.jahs.context.order.modules.order.domain.SerializedCartItems
import java.math.BigDecimal
import java.time.ZonedDateTime

data class OrderRestResponse(val id: String,
                             val createdOn: ZonedDateTime,
                             val userId: String,
                             val detailLines: SerializedCartItems,
                             val billed: Boolean,
                             val total: BigDecimal)
