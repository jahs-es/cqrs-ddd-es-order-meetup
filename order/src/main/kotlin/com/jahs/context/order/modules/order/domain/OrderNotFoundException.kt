package com.jahs.context.order.modules.order.domain

class OrderNotFoundException(id: String): RuntimeException("Order with id <$id> was not found.")
