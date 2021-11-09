/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.hook;

import de.hybris.platform.commerceservices.order.CommerceSaveCartException;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartParameter;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartResult;


/**
 * A method hook to execute custom code before/after cloning a saved cart
 */
public interface CommerceCloneSavedCartMethodHook
{
	/**
	 * Execute custom logic before cloning a saved cart
	 *
	 * @param parameters
	 *           a {@link de.hybris.platform.commerceservices.service.data.CommerceSaveCartParameter} parameter object
	 * @throws de.hybris.platform.commerceservices.order.CommerceSaveCartException
	 *
	 */
	void beforeCloneSavedCart(CommerceSaveCartParameter parameters) throws CommerceSaveCartException;

	/**
	 * Execute custom logic after cloning a saved cart
	 *
	 * @param parameters
	 *           a {@link CommerceSaveCartParameter} parameter object
	 * @param saveCartResult
	 *           {@link de.hybris.platform.commerceservices.service.data.CommerceSaveCartResult}
	 * @throws CommerceSaveCartException
	 */
	void afterCloneSavedCart(CommerceSaveCartParameter parameters, CommerceSaveCartResult saveCartResult)
			throws CommerceSaveCartException;
}
