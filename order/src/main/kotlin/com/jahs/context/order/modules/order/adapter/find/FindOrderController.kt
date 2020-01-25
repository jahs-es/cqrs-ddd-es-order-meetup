package com.jahs.context.order.modules.order.adapter.find

import com.jahs.context.order.modules.order.domain.view.OrderResponse
import com.jahs.context.order.modules.order.domain.view.find.FindOrderQuery
import com.kotato.cqrs.domain.query.QueryBus
import com.kotato.cqrs.domain.query.ask
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
open class FindOrderController(private val queryBus: QueryBus) {

    @GetMapping("/order/order/{orderId}")
    open fun find(@PathVariable("orderId") id: String) =
            queryBus.ask<OrderResponse>(FindOrderQuery(id))
                    .toRestResponse()
                    .let { ResponseEntity.ok(it) }

}
