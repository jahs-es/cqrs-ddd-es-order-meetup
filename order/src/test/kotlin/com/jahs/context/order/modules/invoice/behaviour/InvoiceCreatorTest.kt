package com.jahs.context.order.modules.invoice.behaviour

import com.kotato.assertSimilar.MatcherSimilar.assertSimilar
import com.jahs.context.order.modules.order.domain.calculateTotal
import com.jahs.context.order.modules.order.domain.toCartItems
import com.jahs.context.order.modules.order.domain.view.OrderResponse
import com.jahs.context.order.modules.order.domain.view.find.FindOrderQuery
import com.jahs.context.order.modules.order.domain.view.find.FindOrderQueryAsker
import com.jahs.context.order.modules.order.stub.OrderBilledEventStub
import com.jahs.context.order.modules.order.stub.OrderResponseStub
import com.jahs.context.order.modules.invoice.domain.Invoice
import com.jahs.context.order.modules.invoice.domain.InvoiceId
import com.jahs.context.order.modules.invoice.domain.InvoiceStatus
import com.jahs.context.order.modules.invoice.domain.create.CreateInvoiceOnOrderCheckedOutEventHandler
import com.jahs.context.order.modules.invoice.domain.create.InvoiceCreatedEvent
import com.jahs.context.order.modules.invoice.domain.create.InvoiceCreator
import com.jahs.context.order.modules.invoice.infrastructure.AxonInvoiceRepository
import com.jahs.context.order.modules.invoice.stub.InvoiceCreatedEventStub
import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.user.domain.UserId
import com.kotato.cqrs.domain.query.QueryBus
import com.kotato.cqrs.domain.query.ask
import com.jahs.shared.getPublishedEvents
import com.jahs.shared.loadAggregate
import com.jahs.shared.whenLambda
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.axonframework.test.aggregate.AggregateTestFixture
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class InvoiceCreatorTest {

    private val fixture = AggregateTestFixture(Invoice::class.java)
    private val repository = AxonInvoiceRepository(fixture.repository)
    private val queryBus: QueryBus = mock()
    private val service = InvoiceCreator(repository, FindOrderQueryAsker(queryBus))
    private val handler = CreateInvoiceOnOrderCheckedOutEventHandler(service)

    @Test
    fun `it should create an invoice`() {
        val event = OrderBilledEventStub.random()
        val response = OrderResponseStub.random(orderId = event.aggregateId)
        val price = response.detailLines.toCartItems().calculateTotal()
        val expected = InvoiceCreatedEventStub.random(aggregateId = event.invoiceId,
                                                    orderId = event.aggregateId,
                                                    userId = response.userId,
                                                    price = price.amount,
                                                    currency = price.currency)

        shouldFindOrder(event.aggregateId, response)

        fixture.givenNoPriorActivity()
                .whenLambda({ handler.on(event) })
                .expectSuccessfulHandlerExecution()
                .getPublishedEvents()
                .let {
                    assertTrue { it.size == 1 }
                    val actual = it.first() as InvoiceCreatedEvent
                    assertSimilar(actual.aggregateId(), expected.aggregateId)
                    assertSimilar(actual.orderId, expected.orderId)
                    assertSimilar(actual.price, expected.price)
                    assertSimilar(actual.currency, expected.currency)
                }

        fixture.loadAggregate(event.invoiceId).let {
            assertSimilar(it.invoiceId, InvoiceId.fromString(event.invoiceId))
            assertSimilar(it.orderId, OrderId.fromString(event.aggregateId))
            assertSimilar(it.invoiceStatus, InvoiceStatus.IN_PROGRESS)
            assertSimilar(it.price, price)
            assertSimilar(it.userId, UserId.fromString(expected.userId))
        }
    }

    private fun shouldFindOrder(id: String,
                                response: OrderResponse?) {
        doReturn(response).whenever(queryBus).ask<OrderResponse>(FindOrderQuery(id))
    }

}
