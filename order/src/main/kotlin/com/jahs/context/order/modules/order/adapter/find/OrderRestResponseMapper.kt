package com.jahs.context.order.modules.order.adapter.find

import com.jahs.context.order.modules.order.domain.view.OrderResponse

fun OrderResponse.toRestResponse() =
        OrderRestResponse(orderId, createdOn, userId, detailLines, billed, total)
