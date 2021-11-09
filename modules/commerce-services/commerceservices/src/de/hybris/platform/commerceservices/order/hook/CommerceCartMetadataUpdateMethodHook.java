/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.hook;

import de.hybris.platform.commerceservices.service.data.CommerceCartMetadataParameter;


/**
 * A hook interface into before and after cart metadata (i.e. name, description) update lifecycle
 */
public interface CommerceCartMetadataUpdateMethodHook
{
	/**
	 * Executed before commerce cart metadata update.
	 *
	 * @param parameter
	 *           a bean holding any number of additional attributes a client may want to pass to the method
	 * @throws IllegalArgumentException
	 *            if any attributes fail validation
	 */
	void beforeMetadataUpdate(CommerceCartMetadataParameter parameter);

	/**
	 * Executed after commerce cart metadata update.
	 *
	 * @param parameter
	 *           a bean holding any number of additional attributes a client may want to pass to the method
	 */
	void afterMetadataUpdate(CommerceCartMetadataParameter parameter);
}
