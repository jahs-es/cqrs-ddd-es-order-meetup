package com.jahs.context.order.modules.order.domain.view.add

import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.order.domain.DetailLine
import com.jahs.context.order.modules.order.domain.add
import com.jahs.context.order.modules.order.domain.calculateTotal
import com.jahs.context.order.modules.order.domain.view.OrderView
import com.jahs.context.order.modules.order.domain.view.OrderViewNotFoundException
import com.jahs.context.order.modules.order.domain.view.OrderViewRepository
import com.jahs.shared.transaction.ReadModelTransaction
import javax.inject.Named

@Named
open class OrderViewDetailLineAdder(private val repository: OrderViewRepository) {

    @ReadModelTransaction
    open operator fun invoke(orderId: OrderId, detailLine: DetailLine, quantity: Int) {
        repository.search(orderId)
                .also { assertOrderViewExists(orderId, it) }!!
                .let {
                    it.copy(detailLines = it.detailLines.add(detailLine, quantity))
                }
                .let {
                    it.copy(total = it.detailLines.calculateTotal().amount)
                }
                .let(repository::save)

    }

    private fun assertOrderViewExists(id: OrderId, order: OrderView?) {
        order ?: throw OrderViewNotFoundException(id.asString())
    }

}
