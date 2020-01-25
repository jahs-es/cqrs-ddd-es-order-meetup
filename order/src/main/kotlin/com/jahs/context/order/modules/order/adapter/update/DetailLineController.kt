package com.jahs.context.order.modules.order.adapter.update

import com.jahs.context.order.modules.order.domain.add.AddDetailLineCommand
import com.jahs.context.order.modules.order.domain.subtract.SubtractDetailLineCommand
import com.kotato.cqrs.domain.command.CommandBus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
open class DetailLineController(private val commandBus: CommandBus) {

    @PatchMapping("/order/order/{orderId}")
    open fun update(@PathVariable("orderId") orderId: String,
                    @RequestBody @Valid request: DetailLineRestRequest): ResponseEntity<Unit> {
        if (request.quantity!! < 0) {
            commandBus.handle(SubtractDetailLineCommand(orderId,
                                                      request.productId!!.toString(),
                                                      Math.abs(request.quantity),
                                                      request.price!!,
                                                      request.currency!!))
        } else if (request.quantity > 0) {
            commandBus.handle(AddDetailLineCommand(orderId,
                                                 request.productId!!.toString(),
                                                 request.quantity,
                                                 request.price!!,
                                                 request.currency!!))
        }
        return ResponseEntity.noContent().build()
    }

}
