package com.jahs.context.order.modules.detail_line.domain

import com.jahs.shared.valueobject.ValueObject
import java.io.Serializable
import java.util.UUID
import javax.persistence.Column

@ValueObject
data class ItemId(@Column(columnDefinition = "binary(16)") var id: UUID) : Serializable {

    override fun toString(): String {
        return id.toString()
    }

    fun asString(): String {
        return id.toString()
    }

    companion object {
        fun fromString(uuid: String): ItemId {
            return ItemId(UUID.fromString(uuid))
        }
    }

}
