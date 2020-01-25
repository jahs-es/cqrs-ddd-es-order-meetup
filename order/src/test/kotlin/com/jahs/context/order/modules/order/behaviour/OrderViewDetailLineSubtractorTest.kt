package com.jahs.context.order.modules.order.behaviour

import com.github.javafaker.Faker
import com.jahs.context.order.modules.order.domain.Amount
import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.order.domain.DetailLine
import com.jahs.context.order.modules.order.domain.view.OrderView
import com.jahs.context.order.modules.order.domain.view.OrderViewNotFoundException
import com.jahs.context.order.modules.order.domain.view.OrderViewRepository
import com.jahs.context.order.modules.order.domain.view.subtract.OrderViewDetailLineSubtractor
import com.jahs.context.order.modules.order.domain.view.subtract.SubtractDetailLineOnDetailLineSubtractedEventHandler
import com.jahs.context.order.modules.order.stub.DetalLineSubtractedEventStub
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

class OrderViewDetailLineSubtractorTest {

    private val repository: OrderViewRepository = mock()
    private val service = OrderViewDetailLineSubtractor(repository)
    private val handler = SubtractDetailLineOnDetailLineSubtractedEventHandler(service)

    @Test
    fun `it should subtract line on order view`() {
        val actualAmount = Faker().number().numberBetween(3, 10)
        val amountToSubtract = Faker().number().numberBetween(2, actualAmount - 1)
        val expectedAmount = actualAmount - amountToSubtract
        val event = DetalLineSubtractedEventStub.random(quantity = amountToSubtract)
        val cartItem = DetailLine(ItemId.fromString(event.itemId),
                                Money.of(event.price, event.currency))

        val expectedTotal: BigDecimal = event.price!!.times(BigDecimal(expectedAmount));

        val view = OrderViewStub.random(id = OrderId.fromString(event.aggregateId()),
                detailLines = mapOf(cartItem to Amount(actualAmount)),total = expectedTotal)

        val expected = view.copy(detailLines = mapOf(cartItem to Amount(expectedAmount)))

        shouldSearchOrderView(view.id, view)
        shouldSaveOrderView(expected)

        handler.on(event)

        verify(repository).save(expected)
    }

    @Test
    fun `it should subtract line on order view with same amount as actual has`() {
        val amount = Faker().number().numberBetween(1, 10)
        val event = DetalLineSubtractedEventStub.random(quantity = amount)
        val cartItem = DetailLine(ItemId.fromString(event.itemId),
                                Money.of(event.price, event.currency))
        val view = OrderViewStub.random(id = OrderId.fromString(event.aggregateId()),
                detailLines = mapOf(cartItem to Amount(amount)))
        val expected = view.copy(detailLines = emptyMap())

        shouldSearchOrderView(view.id, view)
        shouldSaveOrderView(expected)

        handler.on(event)

        verify(repository).save(expected)
    }

    @Test
    fun `it should throw exception when adding line on order view`() {
        assertFailsWith<OrderViewNotFoundException> {
            DetalLineSubtractedEventStub.random()
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
