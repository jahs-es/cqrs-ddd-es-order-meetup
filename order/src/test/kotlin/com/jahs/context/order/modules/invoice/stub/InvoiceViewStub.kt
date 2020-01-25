package com.jahs.context.order.modules.invoice.stub

import com.jahs.context.order.modules.invoice.domain.InvoiceId
import com.jahs.context.order.modules.order.domain.DetailLines
import com.jahs.context.order.modules.order.stub.OrderIdStub
import com.jahs.context.order.modules.order.stub.DetalLinesStub
import com.jahs.context.order.modules.invoice.domain.InvoiceStatus
import com.jahs.context.order.modules.invoice.domain.view.InvoiceView
import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.user.domain.UserId
import com.jahs.context.order.modules.user.stub.UserIdStub
import com.jahs.shared.money.Money
import com.jahs.shared.stub.MoneyStub
import java.time.ZonedDateTime

class InvoiceViewStub {
    companion object {
        fun random(id: InvoiceId = InvoiceIdStub.Companion.random(),
                   occurredOn: ZonedDateTime = ZonedDateTime.now(),
                   userId: UserId = UserIdStub.random(),
                   orderId: OrderId = OrderIdStub.random(),
                   price: Money = MoneyStub.random(),
                   status: InvoiceStatus = InvoiceStatusStub.Companion.random(),
                   detailLines: DetailLines = DetalLinesStub.random())
                = InvoiceView(id, occurredOn, userId, orderId, price, status, detailLines)
    }
}
