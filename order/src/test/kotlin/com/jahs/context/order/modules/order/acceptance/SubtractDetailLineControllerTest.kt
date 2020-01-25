package com.jahs.context.order.modules.order.acceptance

import com.kotato.assertSimilar.MatcherSimilar.assertSimilar
import com.jahs.context.order.modules.order.adapter.update.DetailLineRestRequest
import com.jahs.context.order.modules.order.domain.Amount
import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.order.domain.DetailLine
import com.jahs.context.order.modules.order.domain.view.OrderViewRepository
import com.jahs.context.order.modules.order.stub.AddDetalLineRestRequestStub
import com.jahs.context.order.modules.order.stub.OrderIdStub
import com.jahs.context.order.modules.order.stub.CreateOrderRestRequestStub
import com.jahs.context.order.modules.order.stub.SubtractDetalLineRestRequestStub
import com.jahs.context.order.modules.detail_line.domain.ItemId
import com.jahs.shared.ContextStarterTest
import com.jahs.shared.ReadModelTransactionWrapper
import com.jahs.shared.money.Money
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import javax.inject.Inject
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class SubtractDetailLineControllerTest : ContextStarterTest() {

    @Inject private lateinit var readModelTransaction: ReadModelTransactionWrapper
    @Inject private lateinit var repository: OrderViewRepository

    @Test
    fun `it should subtract item to order`() {
        val orderId = CreateOrderRestRequestStub.random().also { orderFlow.createOrder(it) }.id!!
        val addItem = AddDetalLineRestRequestStub.random()
        val subItem = SubtractDetalLineRestRequestStub.random(itemId = addItem.productId!!,
                                                             price = addItem.price!!,
                                                             currency = addItem.currency!!,
                                                             quantity = (addItem.quantity!! - 1) * -1)
        patch(addItem, OrderId(orderId))
                .statusCode(HttpStatus.NO_CONTENT.value())
        patch(subItem, OrderId(orderId))
                .statusCode(HttpStatus.NO_CONTENT.value())
        readModelTransaction { OrderId.fromString(orderId.toString()).let(repository::search) }
                .let {
                    assertNotNull(it)
                    assertTrue { it!!.detailLines.size == 1 }
                    assertSimilar(it!!.detailLines.entries.first().toPair(),
                                  DetailLine(ItemId.fromString(addItem.productId!!.toString()),
                                           Money.of(addItem.price!!, addItem.currency!!)) to Amount(1))
                }
    }

    @Test
    fun `it should subtract item to order with same quantity as actual has`() {
        val orderId = CreateOrderRestRequestStub.random().also { orderFlow.createOrder(it) }.id!!
        val addItem = AddDetalLineRestRequestStub.random()
        val subItem = SubtractDetalLineRestRequestStub.random(itemId = addItem.productId!!,
                                                             price = addItem.price!!,
                                                             currency = addItem.currency!!,
                                                             quantity = (addItem.quantity!!) * -1)
        patch(addItem, OrderId(orderId))
                .statusCode(HttpStatus.NO_CONTENT.value())
        patch(subItem, OrderId(orderId))
                .statusCode(HttpStatus.NO_CONTENT.value())
        readModelTransaction.invoke { OrderId.fromString(orderId.toString()).let(repository::search) }
                .let {
                    assertNotNull(it)
                    assertTrue { it!!.detailLines.isEmpty() }
                }
    }

    @Test
    fun `it should subtract item to order with more quantity than actual has`() {
        val orderId = CreateOrderRestRequestStub.random().also { orderFlow.createOrder(it) }.id!!
        val addItem = AddDetalLineRestRequestStub.random()
        val subItem = SubtractDetalLineRestRequestStub.random(itemId = addItem.productId!!,
                                                             price = addItem.price!!,
                                                             currency = addItem.currency!!,
                                                             quantity = (addItem.quantity!!) * -1)
        patch(addItem, OrderId(orderId))
                .statusCode(HttpStatus.NO_CONTENT.value())
        patch(subItem, OrderId(orderId))
                .statusCode(HttpStatus.NO_CONTENT.value())
        readModelTransaction.invoke { OrderId.fromString(orderId.toString()).let(repository::search) }
                .let {
                    assertNotNull(it)
                    assertTrue { it!!.detailLines.isEmpty() }
                }
    }

    @Test
    fun `it should throw exception when trying to subtract a line because order does not exists`() {
        patch(SubtractDetalLineRestRequestStub.random(), OrderIdStub.random())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
    }

    @Test
    fun `it should throw exception when trying to subtract a line because no item in order`() {
        CreateOrderRestRequestStub.random()
                .also { orderFlow.createOrder(it) }
                .let {
                    patch(SubtractDetalLineRestRequestStub.random(), OrderId(it.id!!))
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                }
    }

    private fun patch(restRequest: DetailLineRestRequest, orderId: OrderId) = RestAssured.given()
            .header("Content-Type", "application/json")
            .body(objectMapper.writeValueAsString(restRequest))
            .`when`()
            .patch("/order/order/${orderId.asString()}")
            .then()

}
