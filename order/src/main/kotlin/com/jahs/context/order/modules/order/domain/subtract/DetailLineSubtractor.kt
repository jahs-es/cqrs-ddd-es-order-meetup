package com.jahs.context.order.modules.order.domain.subtract

import com.jahs.context.order.modules.order.domain.Order
import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.order.domain.DetailLine
import com.jahs.context.order.modules.order.domain.OrderNotFoundException
import com.jahs.context.order.modules.order.domain.create.OrderRepository
import javax.inject.Named

@Named
open class DetailLineSubtractor(private val repository: OrderRepository) {

    operator fun invoke(id: OrderId, detailLine: DetailLine, quantity: Int) {
        repository
                .search(id)
                .also { assertOrderExists(id, it) }!!
                .subtractItem(detailLine, quantity)
    }

    private fun assertOrderExists(id: OrderId, order: Order?) {
        order ?: throw OrderNotFoundException(id.asString())
    }

}
