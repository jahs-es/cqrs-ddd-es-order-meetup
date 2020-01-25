package com.jahs.context.order.modules.order.stub

import com.jahs.context.order.modules.detail_line.domain.ItemId
import java.util.UUID

class ItemIdStub {
    companion object {
        fun random() = ItemId(UUID.randomUUID())
    }
}
