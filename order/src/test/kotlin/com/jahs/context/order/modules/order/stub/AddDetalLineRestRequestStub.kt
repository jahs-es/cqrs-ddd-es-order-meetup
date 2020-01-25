package com.jahs.context.order.modules.order.stub

import com.github.javafaker.Faker
import com.jahs.context.order.modules.order.adapter.update.DetailLineRestRequest
import com.jahs.shared.stub.BigDecimalStub
import java.math.BigDecimal
import java.util.UUID

class AddDetalLineRestRequestStub {
    companion object {
        fun random(itemId: UUID = ItemIdStub.Companion.random().id,
                   price: BigDecimal = BigDecimalStub.random(),
                   currency: String = "EUR",
                   quantity: Int = Faker().number().numberBetween(1, 100)
                  ) = DetailLineRestRequest(itemId, price, currency, quantity)
    }
}
