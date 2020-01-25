package com.jahs.context.order.modules.order.domain.bill

import com.kotato.cqrs.domain.command.Command

data class BillOrderCommand(val orderId: String,
                            val invoiceId: String) : Command
