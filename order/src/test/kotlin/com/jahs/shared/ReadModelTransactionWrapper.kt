package com.jahs.shared

import com.jahs.shared.transaction.ReadModelTransaction
import javax.inject.Named

@Named
open class ReadModelTransactionWrapper {

    @ReadModelTransaction
    open operator fun <T> invoke(lambda: () -> T) = lambda()

}
