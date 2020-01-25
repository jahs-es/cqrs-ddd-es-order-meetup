package com.jahs.context.order.modules.order.domain

import com.jahs.context.order.modules.detail_line.domain.ItemId
import com.jahs.shared.money.Money
import com.jahs.shared.valueobject.ValueObject
import java.io.Serializable
import java.math.BigDecimal
import java.math.MathContext

@ValueObject
data class Amount(val amount: Int) : Serializable {

    operator fun plus(other: Amount) = Amount(this.amount + other.amount)
    operator fun minus(other: Amount) = Amount(this.amount - other.amount)

}

typealias DetailLines = Map<DetailLine, Amount>
typealias SerializedCartItems = Map<Triple<String, String, String>, Int>

fun DetailLines.add(detailLine: DetailLine, quantity: Int) =
        if (this.notHasItem(detailLine)) {
            mapOf(detailLine to Amount(quantity), *this.pairs())
        } else {
            mapOf(*this.filterNot { it.key == detailLine }.pairs(),
                  this.filter { it.key == detailLine }
                          .let { it.keys.first() to (it[it.keys.first()]!! + Amount(quantity)) })
        }

fun DetailLines.subtract(detailLine: DetailLine, quantity: Int) =
        if (this.filter { it.key == detailLine }.let { it[it.keys.first()]!!.amount <= quantity }) {
            mapOf(*this.filterNot { it.key == detailLine }.pairs())
        } else {
            mapOf(*this.filterNot { it.key == detailLine }.pairs(),
                  this.filter { it.key == detailLine }
                          .let { it.keys.first() to (it[it.keys.first()]!! - Amount(quantity)) })
        }

private fun DetailLines.notHasItem(detailLine: DetailLine) =
        this.keys.none { it.itemId == detailLine.itemId && it.price == detailLine.price }

private fun DetailLines.pairs() = this.entries.map { it.key to it.value }.toTypedArray()

fun SerializedCartItems.toCartItems() =
        this.mapKeys {
            DetailLine(ItemId.fromString(it.key.first),
                     Money.of(BigDecimal(it.key.second, MathContext.UNLIMITED),
                              it.key.third))
        }.mapValues { Amount(it.value) }

fun DetailLines.toSerializedCartItems() =
        this.mapKeys {
            it.key.let {
                Triple(it.itemId.asString(),
                       it.price.amount.toPlainString(),
                       it.price.currency)
            }
        }.mapValues { it.value.amount }
