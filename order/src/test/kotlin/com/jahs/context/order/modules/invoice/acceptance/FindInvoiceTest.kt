package com.jahs.context.order.modules.invoice.acceptance

import com.kotato.assertSimilar.MatcherSimilar.assertSimilar
import com.jahs.context.order.modules.order.domain.SerializedCartItems
import com.jahs.context.order.modules.order.domain.toSerializedCartItems
import com.jahs.context.order.modules.order.stub.AddDetalLineRestRequestStub
import com.jahs.context.order.modules.order.stub.OrderIdStub
import com.jahs.context.order.modules.order.stub.CreateOrderRestRequestStub
import com.jahs.context.order.modules.invoice.domain.view.InvoiceViewRepository
import com.jahs.shared.ContextStarterTest
import com.jahs.shared.assertZonedDateTime
import io.restassured.RestAssured
import io.restassured.response.ValidatableResponse
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import javax.inject.Inject
import kotlin.test.assertNotNull

class FindInvoiceTest : ContextStarterTest() {

    @Inject private lateinit var repository: InvoiceViewRepository

    @Test
    fun `it should find an invoice`() {
        val orderId = OrderIdStub.random()
        val createOrder = CreateOrderRestRequestStub.random(orderId.id)
        val addItem = AddDetalLineRestRequestStub.random()

        orderFlow.createOrder(createOrder)
        orderFlow.addLine(addItem, orderId)
        orderFlow.bill(orderId)

        Thread.sleep(200)

        val order = repository.searchByOrderId(orderId).also { assertNotNull(it) }!!


        RestAssured.given()
                .header("Content-Type", "application/json")
                .`when`()
                .get("/order/invoice/${order.id.asString()}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(order.id.asString()))
                .body("user_id", equalTo(order.userId.asString()))
                .body("order_id", equalTo(order.orderId.asString()))
                .body("status", equalTo(order.status.name))
                .assertZonedDateTime("created_on", order.createdOn)
                .assertDetailLines(order.detailLines.toSerializedCartItems())
    }

    private fun ValidatableResponse.assertDetailLines(expected: SerializedCartItems) {
        val actual = this.extract().path<Map<Triple<*, *, *>, Int>>("detail_lines")
        assertSimilar(actual, expected.mapKeys { it.key.toString() })
    }

}
