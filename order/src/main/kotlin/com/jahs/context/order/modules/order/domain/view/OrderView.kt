package com.jahs.context.order.modules.order.domain.view

import com.jahs.context.order.modules.order.domain.OrderId
import com.jahs.context.order.modules.order.domain.DetailLines
import com.jahs.context.order.modules.user.domain.UserId
import org.hibernate.annotations.DynamicUpdate
import java.io.Serializable
import java.math.BigDecimal
import java.time.ZonedDateTime
import javax.persistence.AttributeOverride
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Embedded
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.FetchType

@Entity
@DynamicUpdate
data class OrderView(@EmbeddedId val id: OrderId,
                     val createdOn: ZonedDateTime,
                     @Embedded val userId: UserId,
                     @ElementCollection(fetch = FetchType.EAGER)
                    @AttributeOverride(name = "key.itemId.id",
                                       column = Column(columnDefinition = "binary(16)"))
                    val detailLines: DetailLines = emptyMap(),
                     val billed: Boolean = false,
                     val total: BigDecimal = BigDecimal(0)) : Serializable
