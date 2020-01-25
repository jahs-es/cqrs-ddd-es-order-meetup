package com.jahs.context.order.modules.order.domain.view.find

import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.order.domain.view.OrderView
import com.jahs.context.order.modules.order.domain.view.OrderViewNotFoundException
import com.jahs.context.order.modules.order.domain.view.OrderViewRepository
import javax.inject.Named

@Named
open class OrderFinder(private val repository: OrderViewRepository) {

    operator fun invoke(id: OrderId) =
            repository.search(id).also { assertOrderExists(id, it) }!!

    private fun assertOrderExists(id: OrderId, entity: OrderView?) {
        entity ?: throw OrderViewNotFoundException(id.asString())
    }

}
