package com.jahs.context.order.modules.invoice.stub

import com.jahs.context.order.modules.invoice.domain.InvoiceId
import java.util.UUID

class InvoiceIdStub {
    companion object {
        fun random() = InvoiceId(UUID.randomUUID())
    }
}
