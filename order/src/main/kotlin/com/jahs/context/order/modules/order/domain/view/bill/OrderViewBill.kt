package com.jahs.context.order.modules.order.domain.view.bill

import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.order.domain.view.OrderView
import com.jahs.context.order.modules.order.domain.view.OrderViewNotFoundException
import com.jahs.context.order.modules.order.domain.view.OrderViewRepository
import com.jahs.shared.transaction.ReadModelTransaction
import javax.inject.Named

@Named
open class OrderViewBill(private val repository: OrderViewRepository) {

    @ReadModelTransaction
    open operator fun invoke(orderId: OrderId) {
        repository.search(orderId)
                .also { assertOrderViewExists(orderId, it) }!!
                .copy(billed = true)
                .let(repository::save)

    }

    private fun assertOrderViewExists(id: OrderId, order: OrderView?) {
        order ?: throw OrderViewNotFoundException(id.asString())
    }

}
