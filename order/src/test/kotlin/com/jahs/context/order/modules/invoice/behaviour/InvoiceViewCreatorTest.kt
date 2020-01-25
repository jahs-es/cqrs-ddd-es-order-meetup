package com.jahs.context.order.modules.invoice.behaviour

import com.jahs.context.order.modules.invoice.domain.InvoiceId
import com.jahs.context.order.modules.order.domain.toCartItems
import com.jahs.context.order.modules.order.domain.view.OrderResponse
import com.jahs.context.order.modules.order.domain.view.OrderViewNotFoundException
import com.jahs.context.order.modules.order.domain.view.find.FindOrderQuery
import com.jahs.context.order.modules.order.domain.view.find.FindOrderQueryAsker
import com.jahs.context.order.modules.order.stub.OrderResponseStub
import com.jahs.context.order.modules.invoice.domain.InvoiceStatus
import com.jahs.context.order.modules.invoice.domain.view.InvoiceView
import com.jahs.context.order.modules.invoice.domain.view.InvoiceViewRepository
import com.jahs.context.order.modules.invoice.domain.view.create.CreateInvoiceViewOnInvoiceCreatedEventHandler
import com.jahs.context.order.modules.invoice.domain.view.create.InvoiceViewCreator
import com.jahs.context.order.modules.invoice.stub.InvoiceCreatedEventStub
import com.jahs.context.order.modules.invoice.stub.InvoiceViewStub
import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.user.domain.UserId
import com.kotato.cqrs.domain.query.QueryBus
import com.kotato.cqrs.domain.query.ask
import com.jahs.shared.MockitoHelper.Companion.mockObject
import com.jahs.shared.money.Money
import com.nhaarman.mockito_kotlin.doNothing
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.doThrow
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import kotlin.test.assertFailsWith

class InvoiceViewCreatorTest {

    private val repository: InvoiceViewRepository = mockObject()
    private val queryBus: QueryBus = mockObject()
    private val asker = FindOrderQueryAsker(queryBus)
    private val creator = InvoiceViewCreator(repository, asker)
    private val handler = CreateInvoiceViewOnInvoiceCreatedEventHandler(creator)

    @Test
    fun `it should create order view on order created event raised`() {
        val event = InvoiceCreatedEventStub.random()
        val response = OrderResponseStub.random(orderId = event.orderId)
        val expected = InvoiceViewStub.random(id = InvoiceId.fromString(event.aggregateId),
                                            occurredOn = event.occurredOn,
                                            userId = UserId.fromString(event.userId),
                                            orderId = OrderId.fromString(event.orderId),
                                            price = Money.of(event.price, event.currency),
                                            status = InvoiceStatus.IN_PROGRESS,
                detailLines = response.detailLines.toCartItems())

        shouldSearchOrder(event.orderId, response)
        shouldSaveView(expected)

        handler.on(event)

        verify(repository).save(expected)
    }

    @Test
    fun `it should throw exception when trying to save order view`() {
        val event = InvoiceCreatedEventStub.random()

        shouldThrowOrderViewNotFoundException(event.orderId)

        assertFailsWith<OrderViewNotFoundException> {
            handler.on(event)
        }

    }

    private fun shouldSaveView(expected: InvoiceView) {
        doNothing().whenever(repository).save(expected)
    }

    private fun shouldSearchOrder(id: String, response: OrderResponse) {
        doReturn(response)
                .whenever(queryBus)
                .ask<OrderResponse>(FindOrderQuery(id))
    }

    private fun shouldThrowOrderViewNotFoundException(id: String) {
        doThrow(OrderViewNotFoundException::class)
                .whenever(queryBus)
                .ask<OrderResponse>(FindOrderQuery(id))
    }

}
