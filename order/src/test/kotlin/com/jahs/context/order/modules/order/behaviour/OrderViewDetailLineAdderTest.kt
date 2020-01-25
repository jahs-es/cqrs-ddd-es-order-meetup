package com.jahs.context.order.modules.order.behaviour

import com.jahs.context.order.modules.order.domain.Amount
import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.order.domain.DetailLine
import com.jahs.context.order.modules.order.domain.view.OrderView
import com.jahs.context.order.modules.order.domain.view.OrderViewNotFoundException
import com.jahs.context.order.modules.order.domain.view.OrderViewRepository
import com.jahs.context.order.modules.order.domain.view.add.AddDetailLineOnDetailLineAddedEventHandler
import com.jahs.context.order.modules.order.domain.view.add.OrderViewDetailLineAdder
import com.jahs.context.order.modules.order.stub.DetalLineAddedEventStub
import com.jahs.context.order.modules.order.stub.OrderViewStub
import com.jahs.context.order.modules.detail_line.domain.ItemId
import com.jahs.shared.money.Money
import com.nhaarman.mockito_kotlin.doNothing
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertFailsWith

class OrderViewDetailLineAdderTest {

    private val repository: OrderViewRepository = mock()
    private val service = OrderViewDetailLineAdder(repository)
    private val handler = AddDetailLineOnDetailLineAddedEventHandler(service)

    @Test
    fun `it should add item on order view`() {
        val view = OrderViewStub.random(detailLines = emptyMap())
        val event = DetalLineAddedEventStub.random(aggregateId = view.id.asString())

        val expectedTotal: BigDecimal = event.price!!.times(BigDecimal(event.quantity!!));

        val expected = view.copy(detailLines = mapOf(DetailLine(ItemId.fromString(event.itemId),
                                                            Money.of(event.price, event.currency)) to Amount(event.quantity)), total = expectedTotal)

        shouldSearchOrderView(view.id, view)
        shouldSaveOrderView(expected)

        handler.on(event)

        verify(repository).save(expected)
    }

    @Test
    fun `it should add item on order view for existent line in view`() {
        val event = DetalLineAddedEventStub.random()
        val expectedTotal: BigDecimal = event.price!!.times(BigDecimal(event.quantity!!));

        val cartItem = DetailLine(ItemId.fromString(event.itemId),
                                Money.of(event.price, event.currency))

        val view = OrderViewStub.random(id = OrderId.fromString(event.aggregateId()),
                detailLines = mapOf(cartItem to Amount(event.quantity)), total = expectedTotal)

        val expected = view.copy(detailLines = mapOf(cartItem to Amount(event.quantity * 2)), total = expectedTotal.times(BigDecimal(2)))

        shouldSearchOrderView(view.id, view)
        shouldSaveOrderView(expected)

        handler.on(event)

        verify(repository).save(expected)
    }

    @Test
    fun `it should throw exception when adding line on order view`() {
        assertFailsWith<OrderViewNotFoundException> {
            DetalLineAddedEventStub.random()
                    .also { shouldSearchOrderView(OrderId.fromString(it.aggregateId()), null) }
                    .let(handler::on)
        }
    }

    private fun shouldSaveOrderView(order: OrderView) {
        doNothing().whenever(repository).save(order)
    }

    private fun shouldSearchOrderView(id: OrderId, order: OrderView?) {
        doReturn(order).whenever(repository).search(id)
    }

}
