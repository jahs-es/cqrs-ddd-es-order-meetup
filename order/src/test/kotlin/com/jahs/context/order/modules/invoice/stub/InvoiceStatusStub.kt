package com.jahs.context.order.modules.invoice.stub

import com.github.javafaker.Faker
import com.jahs.context.order.modules.invoice.domain.InvoiceStatus

class InvoiceStatusStub {
    companion object {
        fun random() = InvoiceStatus.values().let { it[Faker().number().numberBetween(0, it.size)] }
    }
}
