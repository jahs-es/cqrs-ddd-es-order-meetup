package com.jahs.context.order.modules.invoice.stub

import com.jahs.context.order.modules.order.domain.SerializedCartItems
import com.jahs.context.order.modules.order.domain.toSerializedCartItems
import com.jahs.context.order.modules.order.stub.OrderIdStub
import com.jahs.context.order.modules.order.stub.DetalLinesStub
import com.jahs.context.order.modules.invoice.domain.view.InvoiceResponse
import com.jahs.context.order.modules.user.stub.UserIdStub
import java.time.ZonedDateTime

class InvoiceResponseStub {
    companion object {
        fun random(id: String = OrderIdStub.Companion.random().asString(),
                   createdAt: ZonedDateTime = ZonedDateTime.now(),
                   userId: String = UserIdStub.random().asString(),
                   orderId: String = OrderIdStub.random().asString(),
                   status: String = InvoiceStatusStub.Companion.random().name,
                   detailLines: SerializedCartItems = DetalLinesStub.random().toSerializedCartItems())
                = InvoiceResponse(id, createdAt, userId, orderId, status, detailLines)
    }
}
