package com.jahs.context.order.modules.order.domain.create

import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.user.domain.UserId
import javax.inject.Named

@Named
open class OrderCreator(private val repository: OrderRepository) {

    operator fun invoke(id: OrderId, userId: UserId) {
        repository.new(id, userId)
    }

}
