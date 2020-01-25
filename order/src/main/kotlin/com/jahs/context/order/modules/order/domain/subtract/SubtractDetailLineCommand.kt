package com.jahs.context.order.modules.order.domain.subtract

import com.kotato.cqrs.domain.command.Command
import java.math.BigDecimal

data class SubtractDetailLineCommand(val id: String,
                                     val itemId: String,
                                     val quantity: Int,
                                     val price: BigDecimal,
                                     val currency: String): Command
