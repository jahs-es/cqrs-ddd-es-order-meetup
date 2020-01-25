package com.jahs.context.order.modules.order.domain.add

import com.kotato.cqrs.domain.command.Command
import java.math.BigDecimal

data class AddDetailLineCommand(val orderId: String,
                                val itemId: String,
                                val quantity: Int,
                                val price: BigDecimal,
                                val currency: String): Command
