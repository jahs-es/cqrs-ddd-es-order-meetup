package com.jahs.context.order.modules.order.stub

import com.github.javafaker.Faker
import com.jahs.context.order.modules.order.domain.add.DetailLineAddedEvent
import com.jahs.shared.stub.BigDecimalStub
import java.math.BigDecimal
import java.time.ZonedDateTime

class DetalLineAddedEventStub {
    companion object {
        fun random(aggregateId: String = OrderIdStub.Companion.random().asString(),
                   occurredOn: ZonedDateTime = ZonedDateTime.now(),
                   itemId: String = ItemIdStub.Companion.random().asString(),
                   quantity: Int = Faker().number().numberBetween(0, 100),
                   price: BigDecimal = BigDecimalStub.random(),
                   currency: String = "EUR") =
                DetailLineAddedEvent(aggregateId, occurredOn, itemId, quantity, price, currency)
    }
}
