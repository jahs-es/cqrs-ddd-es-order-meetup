package com.jahs.context.order.modules.invoice.domain

import com.jahs.shared.valueobject.ValueObject
import java.io.Serializable
import java.util.UUID
import javax.persistence.Column

@ValueObject
data class InvoiceId(@Column(columnDefinition = "binary(16)") val id: UUID) : Serializable {

    override fun toString() = id.toString()

    fun asString() = id.toString()

    companion object {
        fun fromString(uuid: String) = InvoiceId(UUID.fromString(uuid))
    }

}
