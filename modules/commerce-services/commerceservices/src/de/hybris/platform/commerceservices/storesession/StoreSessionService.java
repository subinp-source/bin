/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.storesession;

/**
 * Defines an API for current language and currency 
 */
public interface StoreSessionService
{
	/**
	 * Sets the current language and validates, if language chosen is supported for current store.
	 *
	 * @param isocode
	 *           language iso
	 */
	void setCurrentLanguage(String isocode);

	/**
	 * Sets the current currency and validates, if currency chosen is supported for current currency.
	 *
	 * @param isocode
	 *           currency iso
	 */
	void setCurrentCurrency(String isocode);

}
