package com.jahs.context.order.modules.order.stub

import com.jahs.context.order.modules.order.domain.view.find.FindOrderQuery

class FindOrderQueryStub {
    companion object {
        fun random() = FindOrderQuery(OrderIdStub.Companion.random().asString())
    }
}
