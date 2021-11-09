/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesetaxinvoicefacades.facades;

import de.hybris.platform.acceleratorfacades.order.AcceleratorCheckoutFacade;
import de.hybris.platform.chinesetaxinvoicefacades.data.TaxInvoiceData;
import de.hybris.platform.chinesetaxinvoiceservices.enums.InvoiceRecipientType;

import java.util.List;


/**
 * Implementation for {@link AcceleratorCheckoutFacade}. Delivers main functionality for chinese tax invoice checkout.
 */
public interface TaxInvoiceCheckoutFacade extends AcceleratorCheckoutFacade
{

	/**
	 * Saves TaxInvoice in AbstractOrderModel.
	 *
	 * @param data
	 *           TaxInvoice data.
	 * @return true if setting TaxInvoiceData successfully, false otherwise
	 */
	boolean setTaxInvoice(TaxInvoiceData data);

	/**
	 * Removes a TaxInvoiceModel for PK.
	 *
	 * @param code
	 *           TaxInvoice code(PK)
	 * @return removes TaxInvoiceData successfully or not
	 */
	boolean removeTaxInvoice(String code);

	/**
	 * Checks if the current CartModel has an TaxInvoice.
	 *
	 * @return true if the current CartModel has an tax invoice, false otherwise
	 */
	boolean hasTaxInvoice();

	/**
	 * Gets all InvoiceRecipientType.
	 *
	 * @return all tax invoice recipient types
	 */
	List<InvoiceRecipientType> getTaxInvoiceRecipientTypes();
}
