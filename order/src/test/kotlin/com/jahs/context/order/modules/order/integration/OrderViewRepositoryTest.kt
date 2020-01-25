package com.jahs.context.order.modules.order.integration

import com.kotato.assertSimilar.MatcherSimilar.assertSimilar
import com.jahs.context.order.modules.order.domain.view.OrderViewRepository
import com.jahs.context.order.modules.order.stub.OrderViewStub
import com.jahs.shared.ContextStarterTest
import com.jahs.shared.ReadModelTransactionWrapper
import org.junit.jupiter.api.Test
import javax.inject.Inject

open class OrderViewRepositoryTest : ContextStarterTest() {

    @Inject private lateinit var repository: OrderViewRepository
    @Inject private lateinit var readModelTransaction: ReadModelTransactionWrapper

    @Test
    open fun `it should save order view`() {
        val view = OrderViewStub.random()

        readModelTransaction { repository.save(view) }

        assertSimilar(view, repository.search(view.id))
    }

}
