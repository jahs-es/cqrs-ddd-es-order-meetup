package com.jahs.context.order.modules.order

import com.fasterxml.jackson.databind.ObjectMapper
import com.jahs.context.order.modules.invoice.domain.InvoiceId
import com.jahs.context.order.modules.order.adapter.create.CreateOrderRestRequest
import com.jahs.context.order.modules.order.adapter.update.DetailLineRestRequest
import com.jahs.context.order.modules.order.domain.OrderId
import io.restassured.RestAssured
import org.springframework.http.HttpStatus

class OrderFlow(private val objectMapper: ObjectMapper) {

    fun createOrder(restRequest: CreateOrderRestRequest) {
        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(objectMapper.writeValueAsString(restRequest))
                .`when`()
                .post("/order/order")
                .then()
                .statusCode(HttpStatus.CREATED.value())
    }

    fun addLine(restRequest: DetailLineRestRequest, orderId: OrderId) {
        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(objectMapper.writeValueAsString(restRequest))
                .`when`()
                .patch("/order/order/${orderId.asString()}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
    }

    fun subtractLine(restRequest: DetailLineRestRequest, orderId: OrderId) {
        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(objectMapper.writeValueAsString(restRequest))
                .`when`()
                .patch("/order/order/${orderId.asString()}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
    }

    fun bill(id: OrderId) {
        RestAssured.given()
                .header("Content-Type", "application/json")
                .`when`()
                .post("/order/order/$id/bill")
                .then()
                .statusCode(HttpStatus.CREATED.value())
    }
}
