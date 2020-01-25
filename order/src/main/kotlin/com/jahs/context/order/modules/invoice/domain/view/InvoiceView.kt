package com.jahs.context.order.modules.invoice.domain.view

import com.jahs.context.order.modules.invoice.domain.InvoiceId
import com.jahs.context.order.modules.order.domain.DetailLines
import com.jahs.context.order.modules.invoice.domain.InvoiceStatus
import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.user.domain.UserId
import com.jahs.shared.money.Money
import org.hibernate.annotations.DynamicUpdate
import java.io.Serializable
import java.time.ZonedDateTime
import javax.persistence.AttributeOverride
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Embedded
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType

@Entity
@DynamicUpdate
data class InvoiceView(@EmbeddedId val id: InvoiceId,
                       val createdOn: ZonedDateTime,
                       @Embedded val userId: UserId,
                       @Embedded val orderId: OrderId,
                       @Embedded val price: Money,
                       @Enumerated(EnumType.STRING) val status: InvoiceStatus,
                       @ElementCollection(fetch = FetchType.EAGER)
                     @AttributeOverride(name = "key.itemId.id",
                                        column = Column(columnDefinition = "binary(16)"))
                     val detailLines: DetailLines) : Serializable {
}
