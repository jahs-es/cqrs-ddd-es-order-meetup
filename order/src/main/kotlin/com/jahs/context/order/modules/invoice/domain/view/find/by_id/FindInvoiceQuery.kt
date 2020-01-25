package com.jahs.context.order.modules.invoice.domain.view.find.by_id

import com.kotato.cqrs.domain.query.Query

data class FindInvoiceQuery(val id: String) : Query
