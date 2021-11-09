/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesetaxinvoiceaddon.controllers;



public interface ControllerConstants
{
	String ADDON_PREFIX = "addon:/chinesetaxinvoiceaddon/";

	interface Views
	{

		interface MultiStepCheckout
		{
			String CHINESE_TAX_INVOICE_PAGE = ADDON_PREFIX + "pages/checkout/multi/chineseTaxInvoicePage";
		}
	}
}
