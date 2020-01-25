package com.jahs.context.order.modules.order.stub

import com.jahs.context.order.modules.order.domain.Amount

class DetalLinesStub {
    companion object {
        fun random() = (1..3).map { DetalLineStub.Companion.random() to Amount(it) }.toMap()
    }
}
