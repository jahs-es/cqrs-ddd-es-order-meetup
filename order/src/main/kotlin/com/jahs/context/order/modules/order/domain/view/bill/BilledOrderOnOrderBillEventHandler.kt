package com.jahs.context.order.modules.order.domain.view.bill

import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.order.domain.bill.OrderBillEvent
import org.axonframework.eventhandling.EventHandler
import javax.inject.Named

@Named
open class BilledOrderOnOrderBillEventHandler(private val bill: OrderViewBill) {

    @EventHandler
    fun on(event: OrderBillEvent) {
        bill(OrderId.fromString(event.aggregateId()))
    }
}
