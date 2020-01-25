package com.jahs.context.order.modules.invoice.domain

class InvoiceNotFoundException(id: String) : RuntimeException("Invoice for id <$id> was not found.")
