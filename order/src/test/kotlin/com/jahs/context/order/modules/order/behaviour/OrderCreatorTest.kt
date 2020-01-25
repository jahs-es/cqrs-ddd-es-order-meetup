package com.jahs.context.order.modules.order.behaviour

import com.jahs.context.order.modules.order.domain.Order
import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.order.domain.create.OrderCreator
import com.jahs.context.order.modules.order.domain.create.CreateOrderCommandHandler
import com.jahs.context.order.modules.order.infrastructure.AxonOrderRepository
import com.jahs.context.order.modules.order.stub.OrderCreatedEventStub
import com.jahs.context.order.modules.order.stub.CreateOrderCommandStub
import com.jahs.context.order.modules.user.domain.UserId
import com.jahs.shared.expectDomainEvent
import com.jahs.shared.loadAggregate
import org.axonframework.test.aggregate.AggregateTestFixture
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class OrderCreatorTest {

    private val fixture = AggregateTestFixture(Order::class.java)
    private val repository = AxonOrderRepository(fixture.repository)
    private val service = OrderCreator(repository)
    private val handler = CreateOrderCommandHandler(service)

    @BeforeEach
    fun setUp() {
        fixture.registerAnnotatedCommandHandler(handler)
    }

    @Test
    fun `it should create a order`() {
        val command = CreateOrderCommandStub.random()
        val expectedEvent = OrderCreatedEventStub.random(aggregateId = command.id,
                                                        userId = command.userId)

        fixture.givenNoPriorActivity()
                .`when`(command)
                .expectSuccessfulHandlerExecution()
                .expectDomainEvent(expectedEvent)

        fixture.loadAggregate(command.id)
                .let { aggregate ->
                    assertEquals(OrderId.fromString(command.id), aggregate.id)
                    assertEquals(UserId.fromString(command.userId), aggregate.userId)
                }

    }

}
