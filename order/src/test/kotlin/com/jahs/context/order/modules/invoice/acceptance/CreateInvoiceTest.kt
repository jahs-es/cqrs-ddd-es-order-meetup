package com.jahs.context.order.modules.invoice.acceptance

import com.kotato.assertSimilar.MatcherSimilar.assertSimilar
import com.jahs.context.order.modules.order.adapter.update.DetailLineRestRequest
import com.jahs.context.order.modules.order.domain.Amount
import com.jahs.context.order.modules.order.domain.DetailLine
import com.jahs.context.order.modules.order.stub.AddDetalLineRestRequestStub
import com.jahs.context.order.modules.order.stub.OrderIdStub
import com.jahs.context.order.modules.order.stub.CreateOrderRestRequestStub
import com.jahs.context.order.modules.detail_line.domain.ItemId
import com.jahs.context.order.modules.invoice.domain.view.InvoiceViewRepository
import com.jahs.context.order.modules.user.domain.UserId
import com.jahs.shared.ContextStarterTest
import com.jahs.shared.money.Money
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime
import javax.inject.Inject
import kotlin.test.assertNotNull

class CreateInvoiceTest : ContextStarterTest() {

    @Inject private lateinit var repository: InvoiceViewRepository

    @Test
    fun `it should create an invoice`() {
        val orderId = OrderIdStub.random()
        val createOrder = CreateOrderRestRequestStub.random(orderId.id)
        val addItem = AddDetalLineRestRequestStub.random()
        orderFlow.createOrder(createOrder)
        orderFlow.addLine(addItem, orderId)
        orderFlow.bill(orderId)

        Thread.sleep(200)

        repository.searchByOrderId(orderId)
                .also { assertNotNull(it) }!!
                .let {
                    assertSimilar(it.orderId, orderId)
                    assertSimilar(it.userId, UserId(createOrder.userId!!))
                    assertSimilar(it.createdOn, ZonedDateTime.now())
                    assertSimilar(it.detailLines, addItem.getCartItems())
                    assertSimilar(it.price, Money.of(addItem.price!!, addItem.currency!!) * Amount(addItem.quantity!!))
                }

    }

    private fun DetailLineRestRequest.getCartItems() =
            mapOf(DetailLine(ItemId(productId!!), Money.of(price!!, currency!!)) to Amount(quantity!!))

}
