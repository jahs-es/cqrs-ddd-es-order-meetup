package com.jahs.context.order.modules.order.adapter.update

import java.math.BigDecimal
import java.util.UUID
import javax.validation.constraints.NotNull

data class DetailLineRestRequest(
        @field:NotNull val productId: UUID?,
        @field:NotNull val price: BigDecimal?,
        @field:NotNull val currency: String?,
        @field:NotNull val quantity: Int?
                                      )
