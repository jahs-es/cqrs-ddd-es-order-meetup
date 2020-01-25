package com.jahs.context.order.modules.order.domain.view.subtract

import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.order.domain.DetailLine
import com.jahs.context.order.modules.order.domain.calculateTotal
import com.jahs.context.order.modules.order.domain.subtract
import com.jahs.context.order.modules.order.domain.view.OrderView
import com.jahs.context.order.modules.order.domain.view.OrderViewNotFoundException
import com.jahs.context.order.modules.order.domain.view.OrderViewRepository
import com.jahs.shared.transaction.ReadModelTransaction
import java.math.BigDecimal
import javax.inject.Named

@Named
open class OrderViewDetailLineSubtractor(private val repository: OrderViewRepository) {

    @ReadModelTransaction
    open operator fun invoke(orderId: OrderId, detailLine: DetailLine, quantity: Int) {
        repository.search(orderId)
                .also { assertOrderViewExists(orderId, it) }!!
                .let {
                    it.copy(detailLines = it.detailLines.subtract(detailLine, quantity))
                }
                .let {
                    if (it.detailLines.size > 0) it.copy(total = it.detailLines.calculateTotal().amount) else it.copy(total = BigDecimal(0))
                }
                .let(repository::save)

    }

    private fun assertOrderViewExists(id: OrderId, order: OrderView?) {
        order ?: throw OrderViewNotFoundException(id.asString())
    }

}
