package com.jahs.context.order.modules.order.domain.view.create

import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.order.domain.view.OrderView
import com.jahs.context.order.modules.order.domain.view.OrderViewRepository
import com.jahs.context.order.modules.user.domain.UserId
import com.jahs.shared.transaction.ReadModelTransaction
import java.time.ZonedDateTime
import javax.inject.Named

@Named
open class OrderViewCreator(private val repository: OrderViewRepository) {

    @ReadModelTransaction
    open operator fun invoke(id: OrderId, createdAt: ZonedDateTime, userId: UserId) {
        OrderView(id, createdAt, userId).let(repository::save)
    }

}
