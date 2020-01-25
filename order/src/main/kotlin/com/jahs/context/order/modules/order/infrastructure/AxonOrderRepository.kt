package com.jahs.context.order.modules.order.infrastructure

import com.jahs.context.order.modules.order.domain.Order
import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.order.domain.create.OrderRepository
import com.jahs.context.order.modules.user.domain.UserId
import org.axonframework.commandhandling.model.AggregateNotFoundException
import org.springframework.stereotype.Repository
import org.axonframework.commandhandling.model.Repository as AggregateRepository

@Repository
open class AxonOrderRepository(private val persistenceRepository: AggregateRepository<Order>) : OrderRepository {
    override fun new(id: OrderId, userId: UserId) {
        persistenceRepository.newInstance {
            Order.create(id, userId)
            Order()
        }
    }

    override fun search(id: OrderId): Order? =
            try {
                persistenceRepository.load(id.asString()).invoke { it }
            } catch (exception: AggregateNotFoundException) {
                null
            }
}
