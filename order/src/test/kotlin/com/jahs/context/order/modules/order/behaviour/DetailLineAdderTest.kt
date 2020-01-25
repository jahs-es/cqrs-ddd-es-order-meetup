package com.jahs.context.order.modules.order.behaviour

import com.jahs.context.order.modules.order.domain.Order
import com.jahs.context.order.modules.order.domain.OrderNotFoundException
import com.jahs.context.order.modules.order.domain.add.AddDetailLineCommandHandler
import com.jahs.context.order.modules.order.domain.add.DetailLineAddedEvent
import com.jahs.context.order.modules.order.domain.add.DetailLineAdder
import com.jahs.context.order.modules.order.infrastructure.AxonOrderRepository
import com.jahs.context.order.modules.order.stub.AddDetalLineCommandStub
import com.jahs.context.order.modules.order.stub.OrderCreatedEventStub
import com.jahs.shared.expectDomainEvent
import com.jahs.shared.loadAggregate
import org.axonframework.test.aggregate.AggregateTestFixture
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime
import kotlin.test.assertTrue

class DetailLineAdderTest {
    private val fixture = AggregateTestFixture(Order::class.java)
    private val repository = AxonOrderRepository(fixture.repository)
    private val service = DetailLineAdder(repository)
    private val handler = AddDetailLineCommandHandler(service)

    @BeforeEach
    fun setUp() {
        fixture.registerAnnotatedCommandHandler(handler)
    }

    @Test
    fun `it should add a line`() {
        val command = AddDetalLineCommandStub.random()
        val expected = DetailLineAddedEvent(command.orderId,
                                          ZonedDateTime.now(),
                                          command.itemId,
                                          command.quantity,
                                          command.price,
                                          command.currency)
        fixture.given(OrderCreatedEventStub.random(aggregateId = command.orderId))
                .`when`(command)
                .expectSuccessfulHandlerExecution()
                .expectDomainEvent(expected)

        fixture.loadAggregate(command.orderId)
                .let { assertTrue { it.detailLines.size == 1 } }
    }

    @Test
    fun `it should add a line on already existing item on order`() {
        val command = AddDetalLineCommandStub.random()
        val givenCommand = AddDetalLineCommandStub.random(id = command.orderId,
                                                         itemId = command.itemId,
                                                         currency = command.currency,
                                                         price = command.price)
        val expected = DetailLineAddedEvent(command.orderId,
                                          ZonedDateTime.now(),
                                          command.itemId,
                                          command.quantity,
                                          command.price,
                                          command.currency)
        fixture.given(OrderCreatedEventStub.random(aggregateId = command.orderId))
                .andGivenCommands(givenCommand)
                .`when`(command)
                .expectSuccessfulHandlerExecution()
                .expectDomainEvent(expected)

        fixture.loadAggregate(command.orderId)
                .let {
                    assertTrue { it.detailLines.size == 1 }
                    assertTrue { it.detailLines[it.detailLines.keys.first()]!!.amount == (command.quantity + givenCommand.quantity) }
                }
    }

    @Test
    fun `it should add a line on already existing item on order with different price`() {
        val command = AddDetalLineCommandStub.random()
        val givenCommand = AddDetalLineCommandStub.random(id = command.orderId,
                                                         itemId = command.itemId,
                                                         currency = command.currency)
        val expected = DetailLineAddedEvent(command.orderId,
                                          ZonedDateTime.now(),
                                          command.itemId,
                                          command.quantity,
                                          command.price,
                                          command.currency)
        fixture.given(OrderCreatedEventStub.random(aggregateId = command.orderId))
                .andGivenCommands(givenCommand)
                .`when`(command)
                .expectSuccessfulHandlerExecution()
                .expectDomainEvent(expected)

        fixture.loadAggregate(command.orderId)
                .let {
                    assertTrue { it.detailLines.size == 2 }
                }
    }

    @Test
    fun `it should throw exception when trying to add a line`() {
        fixture.givenNoPriorActivity()
                .`when`(AddDetalLineCommandStub.random())
                .expectException(OrderNotFoundException::class.java)
    }
}
