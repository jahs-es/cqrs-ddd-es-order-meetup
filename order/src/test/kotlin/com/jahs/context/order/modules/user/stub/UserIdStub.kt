package com.jahs.context.order.modules.user.stub

import com.jahs.context.order.modules.user.domain.UserId
import java.util.UUID

class UserIdStub {
    companion object {
        fun random() = UserId(UUID.randomUUID())
    }
}
