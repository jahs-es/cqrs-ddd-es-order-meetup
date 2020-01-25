package com.jahs.context.order.modules.order.domain.add

import com.jahs.context.order.modules.order.domain.Order
import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.order.domain.DetailLine
import com.jahs.context.order.modules.order.domain.OrderNotFoundException
import com.jahs.context.order.modules.order.domain.create.OrderRepository
import javax.inject.Named

@Named
open class DetailLineAdder(private val repository: OrderRepository) {

    operator fun invoke(id: OrderId, detailLine: DetailLine, quantity: Int) {
        repository
                .search(id)
                .also { assertOrderExists(id, it) }!!
                .addItem(detailLine, quantity)
    }

    private fun assertOrderExists(id: OrderId, order: Order?) {
        order ?: throw OrderNotFoundException(id.asString())
    }

}
