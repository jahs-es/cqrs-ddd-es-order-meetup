package com.jahs.context.order.modules.order.behaviour

import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.order.domain.view.OrderViewRepository
import com.jahs.context.order.modules.order.domain.view.create.OrderViewCreator
import com.jahs.context.order.modules.order.domain.view.create.CreateOrderViewOnOrderCreatedEventHandler
import com.jahs.context.order.modules.order.stub.OrderCreatedEventStub
import com.jahs.context.order.modules.order.stub.OrderViewStub
import com.jahs.context.order.modules.user.domain.UserId
import com.nhaarman.mockito_kotlin.doNothing
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test

class OrderViewCreatorTest {

    private val repository: OrderViewRepository = mock()
    private val creator = OrderViewCreator(repository)
    private val handler = CreateOrderViewOnOrderCreatedEventHandler(creator)

    @Test
    fun `it should create order view on order created event raised`() {
        val event = OrderCreatedEventStub.random()
        val expected = OrderViewStub.random(OrderId.fromString(event.aggregateId ()),
                                           event.occurredOn(),
                                           UserId.fromString(event.userId))

        doNothing().whenever(repository).save(expected)

        handler.on(event)

        verify(repository).save(expected)
    }

}
