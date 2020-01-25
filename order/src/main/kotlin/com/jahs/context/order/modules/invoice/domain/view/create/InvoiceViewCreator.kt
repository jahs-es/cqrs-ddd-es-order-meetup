package com.jahs.context.order.modules.invoice.domain.view.create

import com.jahs.context.order.modules.invoice.domain.InvoiceId
import com.jahs.context.order.modules.order.domain.toCartItems
import com.jahs.context.order.modules.order.domain.view.find.FindOrderQueryAsker
import com.jahs.context.order.modules.invoice.domain.InvoiceStatus
import com.jahs.context.order.modules.invoice.domain.view.InvoiceView
import com.jahs.context.order.modules.invoice.domain.view.InvoiceViewRepository
import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.user.domain.UserId
import com.jahs.shared.money.Money
import com.jahs.shared.transaction.ReadModelTransaction
import java.time.ZonedDateTime
import javax.inject.Named

@Named
open class InvoiceViewCreator(private val repository: InvoiceViewRepository,
                              private val asker: FindOrderQueryAsker) {

    @ReadModelTransaction
    open operator fun invoke(invoiceId: InvoiceId,
                             createdOn: ZonedDateTime,
                             orderId: OrderId,
                             userId: UserId,
                             price: Money) {
        repository.save(InvoiceView(invoiceId,
                                  createdOn,
                                  userId,
                                  orderId,
                                  price,
                                  InvoiceStatus.IN_PROGRESS,
                                  asker.ask(orderId).detailLines.toCartItems()))
    }

}
