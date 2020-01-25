package com.jahs.context.order.modules.invoice.domain.view

class InvoiceViewNotFoundException(id: String) : RuntimeException("Invoice for id <$id> was not found.")
