package com.jahs.context.order.modules.order.infrastructure.view

import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.order.domain.view.OrderView
import com.jahs.context.order.modules.order.domain.view.OrderViewRepository
import org.springframework.data.jpa.repository.JpaRepository
import javax.inject.Named

@Named
open class JpaOrderViewRepository(private val persistenceRepository: JpaOrderViewPersistenceRepository) : OrderViewRepository {

    override fun save(entity: OrderView) {
        entity.let(persistenceRepository::saveAndFlush)
    }

    override fun search(id: OrderId): OrderView? = id.let(persistenceRepository::findOne)

    interface JpaOrderViewPersistenceRepository : JpaRepository<OrderView, OrderId>
}
