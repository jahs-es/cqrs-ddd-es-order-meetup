package com.jahs.context.order.modules.order.acceptance

import com.kotato.assertSimilar.MatcherSimilar.assertSimilar
import com.jahs.context.order.modules.order.adapter.update.DetailLineRestRequest
import com.jahs.context.order.modules.order.domain.Amount
import com.jahs.context.order.modules.order.domain.DetailLine
import com.jahs.context.order.modules.order.domain.SerializedCartItems
import com.jahs.context.order.modules.order.domain.toSerializedCartItems
import com.jahs.context.order.modules.order.stub.AddDetalLineRestRequestStub
import com.jahs.context.order.modules.order.stub.OrderIdStub
import com.jahs.context.order.modules.order.stub.CreateOrderRestRequestStub
import com.jahs.context.order.modules.detail_line.domain.ItemId
import com.jahs.shared.ContextStarterTest
import com.jahs.shared.assertZonedDateTime
import com.jahs.shared.money.Money
import io.restassured.RestAssured
import io.restassured.response.ValidatableResponse
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.time.ZonedDateTime

class FindOrderTest : ContextStarterTest() {

    @Test
    fun `it should find a order`() {
        val orderId = OrderIdStub.random()
        val createOrderRestRequest = CreateOrderRestRequestStub.random(orderId.id)
        val addItem = AddDetalLineRestRequestStub.random()
        orderFlow.createOrder(createOrderRestRequest)
        orderFlow.addLine(addItem, orderId)

        Thread.sleep(200)

        RestAssured.given()
                .header("Content-Type", "application/json")
                .`when`()
                .get("/order/order/${orderId.asString()}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(orderId.asString()))
                .body("user_id", equalTo(createOrderRestRequest.userId!!.toString()))
                .body("billed", equalTo(false))
                .assertZonedDateTime("created_on", ZonedDateTime.now())
                .assertDetailLines(addItem.toDetailLines().toSerializedCartItems())
    }

    private fun ValidatableResponse.assertDetailLines(expected: SerializedCartItems) {
        val actual = this.extract().path<Map<Triple<*, *, *>, Int>>("detail_lines")
        assertSimilar(actual, expected.mapKeys { it.key.toString() })
    }

    private fun DetailLineRestRequest.toDetailLines() =
            mapOf(DetailLine(ItemId.fromString(this.productId!!.toString()),
                           Money.of(this.price!!, this.currency!!)) to Amount(this.quantity!!))

}
