package com.jahs.context.order.modules.order.acceptance

import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.order.domain.view.OrderViewRepository
import com.jahs.context.order.modules.order.stub.AddDetalLineRestRequestStub
import com.jahs.context.order.modules.order.stub.OrderIdStub
import com.jahs.context.order.modules.order.stub.CreateOrderRestRequestStub
import com.jahs.shared.ContextStarterTest
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import javax.inject.Inject
import kotlin.test.assertTrue

class BillOrderControllerTest : ContextStarterTest() {

    @Inject private lateinit var repository: OrderViewRepository

    @Test
    fun `it should bill order with items`() {
        val orderId = OrderIdStub.random()
        orderFlow.createOrder(CreateOrderRestRequestStub.random(orderId.id))
        orderFlow.addLine(AddDetalLineRestRequestStub.random(), orderId)
        bill(orderId)
                .statusCode(HttpStatus.CREATED.value())

        assertTrue { repository.search(orderId)!!.billed }
    }

    @Test
    fun `it should try to bill order with no items`() {
        val orderId = OrderIdStub.random()
        orderFlow.createOrder(CreateOrderRestRequestStub.random(orderId.id))
        bill(orderId)
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
    }

    @Test
    fun `it should try to bill non existent order`() {
        bill(OrderIdStub.random())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
    }

    private fun bill(id: OrderId) =
            RestAssured.given()
                    .header("Content-Type", "application/json")
                    .`when`()
                    .post("/order/order/$id/bill")
                    .then()

}
