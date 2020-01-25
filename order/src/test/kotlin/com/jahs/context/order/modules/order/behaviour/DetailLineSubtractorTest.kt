package com.jahs.context.order.modules.order.behaviour

import com.jahs.context.order.modules.order.domain.Order
import com.jahs.context.order.modules.order.domain.OrderNotFoundException
import com.jahs.context.order.modules.order.domain.subtract.DetailLineSubtractedEvent
import com.jahs.context.order.modules.order.domain.subtract.DetailLineSubtractor
import com.jahs.context.order.modules.order.domain.subtract.SubtractDetailLineCommand
import com.jahs.context.order.modules.order.domain.subtract.SubtractDetailLineCommandHandler
import com.jahs.context.order.modules.order.infrastructure.AxonOrderRepository
import com.jahs.context.order.modules.order.stub.OrderCreatedEventStub
import com.jahs.context.order.modules.order.stub.DetalLineAddedEventStub
import com.jahs.context.order.modules.order.stub.SubtractDetalLineCommandStub
import com.jahs.shared.expectDomainEvent
import com.jahs.shared.loadAggregate
import org.axonframework.test.aggregate.AggregateTestFixture
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime
import kotlin.test.assertTrue

class DetailLineSubtractorTest {
    private val fixture = AggregateTestFixture(Order::class.java)
    private val repository = AxonOrderRepository(fixture.repository)
    private val service = DetailLineSubtractor(repository)
    private val handler = SubtractDetailLineCommandHandler(service)

    @BeforeEach
    fun setUp() {
        fixture.registerAnnotatedCommandHandler(handler)
    }

    @Test
    fun `it should subtract a line`() {
        val givenCartCreated = OrderCreatedEventStub.random()
        val givenAddCartItem = DetalLineAddedEventStub.random(aggregateId = givenCartCreated.aggregateId(),
                                                             quantity = 2)
        val command = SubtractDetailLineCommand(id = givenCartCreated.aggregateId(),
                                              itemId = givenAddCartItem.itemId,
                                              quantity = givenAddCartItem.quantity - 1,
                                              price = givenAddCartItem.price,
                                              currency = givenAddCartItem.currency)
        val expected = DetailLineSubtractedEvent(command.id,
                                               ZonedDateTime.now(),
                                               command.itemId,
                                               command.quantity,
                                               command.price,
                                               command.currency)
        fixture.given(givenCartCreated, givenAddCartItem)
                .`when`(command)
                .expectSuccessfulHandlerExecution()
                .expectDomainEvent(expected)

        fixture.loadAggregate(command.id)
                .let {
                    assertTrue { it.detailLines.size == 1 }
                    assertTrue { it.detailLines.let { it[it.keys.first()] }!!.amount == 1 }
                }
    }

    @Test
    fun `it should subtract a line with quantity greater than amount in order`() {
        val givenCartCreated = OrderCreatedEventStub.random()
        val givenAddCartItem = DetalLineAddedEventStub.random(aggregateId = givenCartCreated.aggregateId(),
                                                             quantity = 2)
        val command = SubtractDetailLineCommand(id = givenCartCreated.aggregateId(),
                                              itemId = givenAddCartItem.itemId,
                                              quantity = givenAddCartItem.quantity + 1,
                                              price = givenAddCartItem.price,
                                              currency = givenAddCartItem.currency)
        val expected = DetailLineSubtractedEvent(command.id,
                                               ZonedDateTime.now(),
                                               command.itemId,
                                               givenAddCartItem.quantity,
                                               command.price,
                                               command.currency)
        fixture.given(givenCartCreated, givenAddCartItem)
                .`when`(command)
                .expectSuccessfulHandlerExecution()
                .expectDomainEvent(expected)

        fixture.loadAggregate(command.id)
                .let { assertTrue { it.detailLines.isEmpty() } }
    }

    @Test
    fun `it should subtract a line with quantity equal to amount in order`() {
        val givenCartCreated = OrderCreatedEventStub.random()
        val givenAddCartItem = DetalLineAddedEventStub.random(aggregateId = givenCartCreated.aggregateId(),
                                                             quantity = 2)
        val command = SubtractDetailLineCommand(id = givenCartCreated.aggregateId(),
                                              itemId = givenAddCartItem.itemId,
                                              quantity = givenAddCartItem.quantity,
                                              price = givenAddCartItem.price,
                                              currency = givenAddCartItem.currency)
        val expected = DetailLineSubtractedEvent(command.id,
                                               ZonedDateTime.now(),
                                               command.itemId,
                                               command.quantity,
                                               command.price,
                                               command.currency)
        fixture.given(givenCartCreated, givenAddCartItem)
                .`when`(command)
                .expectSuccessfulHandlerExecution()
                .expectDomainEvent(expected)

        fixture.loadAggregate(command.id)
                .let { assertTrue { it.detailLines.isEmpty() } }
    }

    @Test
    fun `it should throw exception when trying to subtract a line that is not in order`() {
        OrderCreatedEventStub.random().let {
            fixture.given()
                    .`when`(SubtractDetalLineCommandStub.random(id = it.aggregateId()))
                    .expectException(OrderNotFoundException::class.java)
        }

    }

    @Test
    fun `it should throw exception when trying to subtract a line`() {
        fixture.givenNoPriorActivity()
                .`when`(SubtractDetalLineCommandStub.random())
                .expectException(OrderNotFoundException::class.java)
    }
}
