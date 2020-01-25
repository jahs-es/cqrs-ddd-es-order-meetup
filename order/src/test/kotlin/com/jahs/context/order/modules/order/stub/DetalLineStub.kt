package com.jahs.context.order.modules.order.stub

import com.jahs.context.order.modules.order.domain.DetailLine
import com.jahs.context.order.modules.detail_line.domain.ItemId
import com.jahs.shared.money.Money
import com.jahs.shared.stub.MoneyStub

class DetalLineStub {
    companion object {
        fun random(itemId: ItemId = ItemIdStub.Companion.random(),
                   price: Money = MoneyStub.random()) = DetailLine(itemId, price)
    }
}
