/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.hook;

import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;


/**
 * A hook strategy to run custom code before restoring a cart
 */
public interface CommerceSaveCartRestorationMethodHook
{

	/**
	 * Execute custom logic before restoring a cart
	 *
	 * @param parameters
	 *           a {@link CommerceCartParameter} parameter object
	 * @throws CommerceCartRestorationException
	 *
	 */
	void beforeRestoringCart(CommerceCartParameter parameters) throws CommerceCartRestorationException;


	/**
	 * Execute custom logic after restoring a cart
	 *
	 * @param parameters
	 *           a {@link CommerceCartParameter} parameter object
	 * @throws CommerceCartRestorationException
	 *
	 */
	void afterRestoringCart(CommerceCartParameter parameters) throws CommerceCartRestorationException;
}
