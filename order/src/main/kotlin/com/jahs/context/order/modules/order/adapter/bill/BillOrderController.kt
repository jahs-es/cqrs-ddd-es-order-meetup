package com.jahs.context.order.modules.order.adapter.bill

import com.jahs.context.order.modules.order.domain.bill.BillOrderCommand
import com.kotato.cqrs.domain.command.CommandBus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.util.UUID

@RestController
open class BillOrderController(private val commandBus: CommandBus) {

    @PostMapping("/order/order/{orderId}/bill")
    open fun bill(@PathVariable("orderId") orderId: String): ResponseEntity<Unit> {
        val invoiceId = UUID.randomUUID().toString()
        commandBus.handle(BillOrderCommand(orderId, invoiceId))
        return ResponseEntity.created(URI("/order/order/$orderId")).build()
    }

}
