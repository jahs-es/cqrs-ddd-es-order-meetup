package com.jahs.context.order.modules.invoice.behaviour

import com.kotato.assertSimilar.MatcherSimilar.assertSimilar
import com.jahs.context.order.modules.order.domain.toSerializedCartItems
import com.jahs.context.order.modules.invoice.domain.InvoiceId
import com.jahs.context.order.modules.invoice.domain.view.InvoiceResponse
import com.jahs.context.order.modules.invoice.domain.view.InvoiceView
import com.jahs.context.order.modules.invoice.domain.view.InvoiceViewNotFoundException
import com.jahs.context.order.modules.invoice.domain.view.InvoiceViewRepository
import com.jahs.context.order.modules.invoice.domain.view.find.by_id.FindInvoiceQuery
import com.jahs.context.order.modules.invoice.domain.view.find.by_id.FindInvoiceQueryHandler
import com.jahs.context.order.modules.invoice.domain.view.find.by_id.InvoiceFinder
import com.jahs.context.order.modules.invoice.stub.InvoiceIdStub
import com.jahs.context.order.modules.invoice.stub.InvoiceViewStub
import com.jahs.shared.MockitoHelper.Companion.mockObject
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.whenever
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class InvoiceFinderTest {

    private val repository: InvoiceViewRepository = mockObject()
    private val service = InvoiceFinder(repository)
    private val handler = FindInvoiceQueryHandler(service)

    @Test
    fun `it should find an invoice`() {
        val view = InvoiceViewStub.random()
        val query = FindInvoiceQuery(view.id.asString())
        val expected = InvoiceResponse(view.id.asString(),
                                     view.createdOn,
                                     view.userId.asString(),
                                     view.orderId.asString(),
                                     view.status.name,
                                     view.detailLines.toSerializedCartItems())

        shouldSearchInvoice(view.id, view)

        assertSimilar(handler.on(query), expected)
    }

    @Test
    fun `it should throw exception when finding an invoice`() {
        val id = InvoiceIdStub.random()
        val query = FindInvoiceQuery(id.asString())

        shouldSearchInvoice(id, null)

        assertFailsWith<InvoiceViewNotFoundException> {
            handler.on(FindInvoiceQuery(id.asString()))
        }
    }

    private fun shouldSearchInvoice(id: InvoiceId, invoice: InvoiceView?) {
        doReturn(invoice).whenever(repository).search(id)
    }

}
