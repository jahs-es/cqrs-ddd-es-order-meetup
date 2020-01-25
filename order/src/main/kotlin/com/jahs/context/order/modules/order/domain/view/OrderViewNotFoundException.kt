package com.jahs.context.order.modules.order.domain.view

class OrderViewNotFoundException(id: String): RuntimeException("Order view with id <$id> was not found.")
