package com.jahs.context.order.modules.order.acceptance

import com.kotato.assertSimilar.MatcherSimilar.assertSimilar
import com.jahs.context.order.modules.order.domain.Amount
import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.order.domain.DetailLine
import com.jahs.context.order.modules.order.domain.view.OrderView
import com.jahs.context.order.modules.order.domain.view.OrderViewRepository
import com.jahs.context.order.modules.order.stub.AddDetalLineRestRequestStub
import com.jahs.context.order.modules.order.stub.OrderIdStub
import com.jahs.context.order.modules.order.stub.CreateOrderRestRequestStub
import com.jahs.context.order.modules.detail_line.domain.ItemId
import com.jahs.context.order.modules.user.domain.UserId
import com.jahs.shared.ContextStarterTest
import com.jahs.shared.ReadModelTransactionWrapper
import com.jahs.shared.money.Money
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import java.time.ZonedDateTime
import javax.inject.Inject
import kotlin.test.assertNotNull

class AddDetailLineControllerTest : ContextStarterTest() {

    @Inject
    private lateinit var readModelTransaction: ReadModelTransactionWrapper
    @Inject
    private lateinit var repository: OrderViewRepository

    @Test
    fun `it should add item to order`() {
        val orderId = OrderIdStub.random()
        val createOrderRestRequest = CreateOrderRestRequestStub.random(orderId = orderId.id)
        orderFlow.createOrder(createOrderRestRequest)

        val restRequest = AddDetalLineRestRequestStub.random()
        val expectedTotal: BigDecimal = restRequest.price!!.times(BigDecimal(restRequest.quantity!!));

        orderFlow.addLine(restRequest, orderId)


        readModelTransaction { repository.search(orderId) }
                .let {
                    assertNotNull(it)
                    val cartItem = DetailLine(ItemId.fromString(restRequest.productId!!.toString()),
                            Money.of(restRequest.price!!, restRequest.currency!!))
                    assertSimilar(it,
                            OrderView(id = OrderId.fromString(createOrderRestRequest.id!!.toString()),
                                    createdOn = ZonedDateTime.now(),
                                    userId = UserId.fromString(createOrderRestRequest.userId!!.toString()),
                                    detailLines = mapOf(cartItem to Amount(restRequest.quantity!!)),
                                    total = expectedTotal))
                }
    }

    @Test
    fun `it should add a line on already existing item on order`() {
        val orderId = OrderIdStub.random()
        val createOrderRestRequest = CreateOrderRestRequestStub.random(orderId = orderId.id)
        orderFlow.createOrder(createOrderRestRequest)

        val restRequest = AddDetalLineRestRequestStub.random()
        var expectedTotal: BigDecimal = BigDecimal(0)

        (0..1).forEach {
            orderFlow.addLine(restRequest, orderId)
            expectedTotal += restRequest.price!!.times(BigDecimal(restRequest.quantity!!));
        }

        readModelTransaction.invoke { repository.search(orderId) }
                .let {
                    assertNotNull(it)
                    val cartItem = DetailLine(ItemId.fromString(restRequest.productId!!.toString()),
                            Money.of(restRequest.price!!, restRequest.currency!!))
                    assertSimilar(it,
                            OrderView(id = OrderId.fromString(createOrderRestRequest.id!!.toString()),
                                    createdOn = ZonedDateTime.now(),
                                    userId = UserId.fromString(createOrderRestRequest.userId!!.toString()),
                                    detailLines = mapOf(cartItem to Amount(restRequest.quantity!! * 2)),
                                    total = expectedTotal))
                }
    }

    @Test
    fun `it should add a line on already existing item on order with different price`() {
        val orderId = OrderIdStub.random()
        val createOrderRestRequest = CreateOrderRestRequestStub.random(orderId = orderId.id)
        orderFlow.createOrder(createOrderRestRequest)

        var expectedTotal: BigDecimal = BigDecimal(0)

        val restRequest = AddDetalLineRestRequestStub.random()
        orderFlow.addLine(restRequest, orderId)
        expectedTotal += restRequest.price!!.times(BigDecimal(restRequest.quantity!!));

        val restRequest2 = AddDetalLineRestRequestStub.random()
        orderFlow.addLine(restRequest2, orderId)
        expectedTotal += restRequest2.price!!.times(BigDecimal(restRequest2.quantity!!));

        readModelTransaction.invoke { repository.search(orderId) }
                .let {
                    assertNotNull(it)
                    val cartItem = DetailLine(ItemId.fromString(restRequest.productId!!.toString()),
                            Money.of(restRequest.price!!, restRequest.currency!!))
                    val cartItem2 = DetailLine(ItemId.fromString(restRequest2.productId!!.toString()),
                            Money.of(restRequest2.price!!, restRequest2.currency!!))
                    assertSimilar(it,
                            OrderView(id = OrderId.fromString(createOrderRestRequest.id!!.toString()),
                                    createdOn = ZonedDateTime.now(),
                                    userId = UserId.fromString(createOrderRestRequest.userId!!.toString()),
                                    detailLines = mapOf(cartItem to Amount(restRequest.quantity!!),
                                            cartItem2 to Amount(restRequest2.quantity!!)),
                                    total = expectedTotal))
                }
    }

    @Test
    fun `it should throw exception when trying to add a line`() {
        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(objectMapper.writeValueAsString(AddDetalLineRestRequestStub.random()))
                .`when`()
                .patch("/order/order/${OrderIdStub.random()}")
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
    }

}
