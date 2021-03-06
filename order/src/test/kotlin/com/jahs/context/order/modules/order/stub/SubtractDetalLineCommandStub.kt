package com.jahs.context.order.modules.order.stub

import com.github.javafaker.Faker
import com.jahs.context.order.modules.order.domain.subtract.SubtractDetailLineCommand
import com.jahs.shared.stub.BigDecimalStub
import java.math.BigDecimal

class SubtractDetalLineCommandStub {
    companion object {
        fun random(id: String = OrderIdStub.Companion.random().asString(),
                   itemId: String = ItemIdStub.Companion.random().asString(),
                   quantity: Int = Faker().number().numberBetween(0, 10),
                   price: BigDecimal = BigDecimalStub.random(),
                   currency: String = "EUR") =
                SubtractDetailLineCommand(id, itemId, quantity, price, currency)
    }
}
