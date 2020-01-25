package com.jahs.context.order.modules.order.domain

import com.jahs.shared.money.Money
import java.math.BigDecimal

fun DetailLines.calculateTotal() =
        this.map { it.key.price.toEur() * it.value }
                .reduce(Money::plus)

//This should be a currency converter
private fun Money.toEur() =
        this.copy(amount = this.amount.convert(this.currency), currency = "EUR")

private fun BigDecimal.convert(currency: String) = this * BigDecimal.ONE
