package com.jahs.context.order.modules.order.domain

import com.jahs.context.order.modules.detail_line.domain.ItemId
import com.jahs.shared.money.Money
import com.jahs.shared.valueobject.ValueObject
import javax.persistence.Embeddable
import javax.persistence.Embedded

@Embeddable
@ValueObject
data class DetailLine(@Embedded val itemId: ItemId,
                      @Embedded val price: Money)
