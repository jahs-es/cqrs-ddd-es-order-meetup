package com.jahs.context.order.modules.order.domain.view

import com.jahs.context.order.modules.order.domain.toSerializedCartItems

fun OrderView.toResponse() =
        OrderResponse(id.asString(),
                     createdOn,
                     userId.asString(),
                     detailLines.toSerializedCartItems(),
                     billed,
                     total)
