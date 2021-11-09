/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesetaxinvoicefacades.populators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.chinesetaxinvoicefacades.data.TaxInvoiceData;
import de.hybris.platform.chinesetaxinvoiceservices.enums.InvoiceCategory;
import de.hybris.platform.chinesetaxinvoiceservices.enums.InvoiceRecipientType;
import de.hybris.platform.chinesetaxinvoiceservices.model.TaxInvoiceModel;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.order.CartModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class CartTaxInvoicePopulatorTest
{

	private CartTaxInvoicePopulator populator;
	private CartData target;

	@Mock
	private CartModel source;
	@Mock
	private TaxInvoiceModel invoice;
	private final PK pk = PK.fromLong(1l);
	private final InvoiceRecipientType recipientType = InvoiceRecipientType.INDIVIDUAL;
	private final InvoiceCategory invoiceCategory = InvoiceCategory.GENERAL;

	private static final String RECIPIENT = "hybris";
	private static final String TAXPAYER_ID = "1234";

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);

		populator = new CartTaxInvoicePopulator();
		target = new CartData();
	}

	@Test
	public void testPopulatorWithNullTaxInvoiceModel()
	{
		given(source.getTaxInvoice()).willReturn(null);
		populator.populate(source, target);
		final TaxInvoiceData taxInvoiceData = target.getTaxInvoice();
		assertNull(taxInvoiceData);
	}

	@Test
	public void testPopulatorWithNotNullTaxInvoiceModel()
	{
		given(source.getTaxInvoice()).willReturn(invoice);
		given(invoice.getPk()).willReturn(pk);
		given(invoice.getRecipient()).willReturn(RECIPIENT);
		given(invoice.getRecipientType()).willReturn(recipientType);
		given(invoice.getCategory()).willReturn(invoiceCategory);
		given(invoice.getTaxpayerID()).willReturn(TAXPAYER_ID);

		populator.populate(source, target);
		final TaxInvoiceData taxInvoiceData = target.getTaxInvoice();
		assertEquals(pk.toString(), taxInvoiceData.getId());
		assertEquals(RECIPIENT, taxInvoiceData.getRecipient());
		assertEquals(recipientType.getCode(), taxInvoiceData.getRecipientType());
		assertEquals(invoiceCategory.getCode(), taxInvoiceData.getCategory());
		assertEquals(TAXPAYER_ID, taxInvoiceData.getTaxpayerID());
	}

}
