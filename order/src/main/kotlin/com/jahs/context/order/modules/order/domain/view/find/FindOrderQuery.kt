package com.jahs.context.order.modules.order.domain.view.find

import com.kotato.cqrs.domain.query.Query

data class FindOrderQuery(val id: String) : Query
