/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.hook;


import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;

public interface CommerceUpdateCartEntryHook
{
	/**
	 *  Executed after commerce update cart entry
	 *
	 * @param parameter
	 * @param result
	 */
	void afterUpdateCartEntry(CommerceCartParameter parameter, CommerceCartModification result);

	/**
	 *
	 * Executed before commerce update cart entry
	 *
	 * @param parameter
	 */
	void beforeUpdateCartEntry(CommerceCartParameter parameter);

}
