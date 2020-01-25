package com.jahs.context.order.modules.order.behaviour

import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.order.domain.view.OrderView
import com.jahs.context.order.modules.order.domain.view.OrderViewRepository
import com.jahs.context.order.modules.order.domain.view.bill.OrderViewBill
import com.jahs.context.order.modules.order.domain.view.bill.BilledOrderOnOrderBillEventHandler
import com.jahs.context.order.modules.order.stub.OrderBilledEventStub
import com.jahs.context.order.modules.order.stub.OrderViewStub
import com.nhaarman.mockito_kotlin.doNothing
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.jupiter.api.Test

class OrderViewOrderBillTest {

    private val repository: OrderViewRepository = mock()
    private val service = OrderViewBill(repository)
    private val handler = BilledOrderOnOrderBillEventHandler(service)

    @Test
    fun `it should bill on order view`() {
        val view = OrderViewStub.random()
        val event = OrderBilledEventStub.random(aggregateId = view.id.asString())
        val expected = view.copy(billed = true)

        shouldSearchOrderView(view.id, view)
        shouldSaveOrderView(expected)

        handler.on(event)

        verify(repository).save(expected)
    }
    
    private fun shouldSaveOrderView(order: OrderView) {
        doNothing().whenever(repository).save(order)
    }

    private fun shouldSearchOrderView(id: OrderId, order: OrderView?) {
        doReturn(order).whenever(repository).search(id)
    }

}
