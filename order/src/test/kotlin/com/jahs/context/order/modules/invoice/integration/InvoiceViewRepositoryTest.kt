package com.jahs.context.order.modules.invoice.integration

import com.kotato.assertSimilar.MatcherSimilar.assertSimilar
import com.jahs.context.order.modules.invoice.domain.view.InvoiceViewRepository
import com.jahs.context.order.modules.invoice.stub.InvoiceViewStub
import com.jahs.shared.ContextStarterTest
import com.jahs.shared.ReadModelTransactionWrapper
import org.junit.jupiter.api.Test
import javax.inject.Inject

open class InvoiceViewRepositoryTest : ContextStarterTest() {

    @Inject private lateinit var repository: InvoiceViewRepository
    @Inject private lateinit var readModelTransaction: ReadModelTransactionWrapper

    @Test
    open fun `it should save invoice view`() {
        val view = InvoiceViewStub.random()

        readModelTransaction { repository.save(view) }

        assertSimilar(view, repository.search(view.id))
    }

    @Test
    open fun `it should search invoice view by orderId`() {
        val view = InvoiceViewStub.random()

        readModelTransaction { repository.save(view) }

        assertSimilar(view, repository.searchByOrderId(view.orderId))
    }
}
