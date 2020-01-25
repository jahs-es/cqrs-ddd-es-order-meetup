package com.jahs.context.order.modules.order.domain.create

import com.kotato.cqrs.domain.command.Command


data class CreateOrderCommand(val id: String, val userId: String) : Command
