package com.jahs.context.order.modules.order.adapter.create

import com.jahs.context.order.modules.order.domain.create.CreateOrderCommand
import com.kotato.cqrs.domain.command.CommandBus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import javax.validation.Valid

@RestController
open class CreateOrderController(private val commandBus: CommandBus) {

    @PostMapping("/order/order")
    open fun create(@RequestBody @Valid request: CreateOrderRestRequest): ResponseEntity<Unit> {
        commandBus.handle(CreateOrderCommand(request.id!!.toString(),
                                            request.userId!!.toString()))
        return ResponseEntity.created(URI("/order/order/${request.id}")).build()
    }

}
