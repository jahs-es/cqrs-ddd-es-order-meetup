package com.jahs.context.order.modules.order.adapter.create

import java.util.UUID
import javax.validation.constraints.NotNull

data class CreateOrderRestRequest(
        @field:NotNull val id: UUID?,
        @field:NotNull val userId: UUID?
                                )
