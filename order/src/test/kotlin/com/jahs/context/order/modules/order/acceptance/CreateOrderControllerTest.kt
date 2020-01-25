package com.jahs.context.order.modules.order.acceptance

import com.kotato.assertSimilar.MatcherSimilar.assertSimilar
import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.order.domain.view.OrderView
import com.jahs.context.order.modules.order.domain.view.OrderViewRepository
import com.jahs.context.order.modules.order.stub.CreateOrderRestRequestStub
import com.jahs.context.order.modules.user.domain.UserId
import com.jahs.shared.ContextStarterTest
import com.jahs.shared.ReadModelTransactionWrapper
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.time.ZonedDateTime
import javax.inject.Inject
import kotlin.test.assertNotNull

class CreateOrderControllerTest : ContextStarterTest() {

    @Inject private lateinit var readModelTransaction: ReadModelTransactionWrapper
    @Inject private lateinit var repository: OrderViewRepository

    @Test
    fun `it should create order`() {
        val restRequest = CreateOrderRestRequestStub.random()
        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(objectMapper.writeValueAsString(restRequest))
                .`when`()
                .post("/order/order")
                .then()
                .statusCode(HttpStatus.CREATED.value())

        readModelTransaction { repository.search(OrderId.fromString(restRequest.id!!.toString())) }
                .let {
                    assertNotNull(it)
                    assertSimilar(it, OrderView(id = OrderId.fromString(restRequest.id!!.toString()),
                                               createdOn = ZonedDateTime.now(),
                                               userId = UserId.fromString(restRequest.userId!!.toString()),
                                               detailLines = emptyMap()))
                }
    }

}
