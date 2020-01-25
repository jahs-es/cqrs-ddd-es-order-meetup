package com.jahs.context.order.modules.order.behaviour

import com.jahs.context.order.modules.order.domain.Order
import com.jahs.context.order.modules.order.domain.OrderNotFoundException
import com.jahs.context.order.modules.order.domain.bill.OrderAlreadyBilledException
import com.jahs.context.order.modules.order.domain.bill.OrderBillEvent
import com.jahs.context.order.modules.order.domain.bill.OrderBill
import com.jahs.context.order.modules.order.domain.bill.OrderIsEmptyException
import com.jahs.context.order.modules.order.domain.bill.BillOrderCommandHandler
import com.jahs.context.order.modules.order.infrastructure.AxonOrderRepository
import com.jahs.context.order.modules.order.stub.OrderBilledEventStub
import com.jahs.context.order.modules.order.stub.OrderCreatedEventStub
import com.jahs.context.order.modules.order.stub.DetalLineAddedEventStub
import com.jahs.context.order.modules.order.stub.BillOrderCommandStub
import com.jahs.shared.expectDomainEvent
import com.jahs.shared.loadAggregate
import org.axonframework.test.aggregate.AggregateTestFixture
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime
import kotlin.test.assertTrue

class OrderBillTest {
    private val fixture = AggregateTestFixture(Order::class.java)
    private val repository = AxonOrderRepository(fixture.repository)
    private val service = OrderBill(repository)
    private val handler = BillOrderCommandHandler(service)

    @BeforeEach
    fun setUp() {
        fixture.registerAnnotatedCommandHandler(handler)
    }

    @Test
    fun `it should bill a order with two items`() {
        val command = BillOrderCommandStub.random()
        val expected = OrderBillEvent(command.orderId,
                                           ZonedDateTime.now(),
                                           command.invoiceId)
        val givenCartCreated = OrderCreatedEventStub.random(aggregateId = command.orderId)
        val givenAddCartItem1 = DetalLineAddedEventStub.random(aggregateId = command.orderId)
        val givenAddCartItem2 = DetalLineAddedEventStub.random(aggregateId = command.orderId)
        fixture.given(givenCartCreated, givenAddCartItem1, givenAddCartItem2)
                .`when`(command)
                .expectSuccessfulHandlerExecution()
                .expectDomainEvent(expected)

        fixture.loadAggregate(command.orderId)
                .let { assertTrue { it.billed } }
    }

    @Test
    fun `it should throw exception when try to bill a order that doesn't exist`() {
        val command = BillOrderCommandStub.random()
        fixture.givenNoPriorActivity()
                .`when`(command)
                .expectException(OrderNotFoundException::class.java)
    }

    @Test
    fun `it should throw exception when try to bill an empty order`() {
        val command = BillOrderCommandStub.random()
        val givenCartCreated = OrderCreatedEventStub.random(aggregateId = command.orderId)
        fixture.given(givenCartCreated)
                .`when`(command)
                .expectException(OrderIsEmptyException::class.java)
    }

    @Test
    fun `it should throw exception when try to bill a order checked out`() {
        val command = BillOrderCommandStub.random()
        val givenCartCreated = OrderCreatedEventStub.random(aggregateId = command.orderId)
        val givenAddCartItem = DetalLineAddedEventStub.random(aggregateId = command.orderId)
        val givenCheckedOut = OrderBilledEventStub.random(aggregateId = command.orderId)
        fixture.given(givenCartCreated, givenAddCartItem, givenCheckedOut)
                .`when`(command)
                .expectException(OrderAlreadyBilledException::class.java)
    }
}
