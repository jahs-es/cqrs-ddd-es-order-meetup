package com.jahs.context.order.modules.order.behaviour

import com.kotato.assertSimilar.MatcherSimilar.assertSimilar
import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.order.domain.toSerializedCartItems
import com.jahs.context.order.modules.order.domain.view.OrderResponse
import com.jahs.context.order.modules.order.domain.view.OrderView
import com.jahs.context.order.modules.order.domain.view.OrderViewNotFoundException
import com.jahs.context.order.modules.order.domain.view.OrderViewRepository
import com.jahs.context.order.modules.order.domain.view.find.OrderFinder
import com.jahs.context.order.modules.order.domain.view.find.FindOrderQuery
import com.jahs.context.order.modules.order.domain.view.find.FindOrderQueryHandler
import com.jahs.context.order.modules.order.stub.OrderViewStub
import com.jahs.context.order.modules.order.stub.FindOrderQueryStub
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class OrderFinderTest {

    private val repository: OrderViewRepository = mock()
    private val service = OrderFinder(repository)
    private val handler = FindOrderQueryHandler(service)

    @Test
    fun `it should find order`() {
        val view = OrderViewStub.random()
        val query = FindOrderQuery(view.id.asString())
        val expected = OrderResponse(view.id.asString(),
                                    view.createdOn,
                                    view.userId.asString(),
                                    view.detailLines.toSerializedCartItems(),
                                    view.billed,
                                    view.total)

        shouldSearchOrder(view.id, view)

        assertSimilar(handler.on(query), expected)
    }

    @Test
    fun `it should throw exception when trying to find order`() {
        val query = FindOrderQueryStub.random()

        shouldSearchOrder(OrderId.fromString(query.id), null)

        assertFailsWith<OrderViewNotFoundException> {
            handler.on(query)
        }
    }

    private fun shouldSearchOrder(id: OrderId, view: OrderView?) {
        doReturn(view).whenever(repository).search(id)
    }
}
