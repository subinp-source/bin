/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.search.validation;

import de.hybris.platform.integrationservices.search.ItemSearchRequest;

/**
 * A validator that ensures presence of the {@link ItemSearchRequest#getRequestedItem()} for unique item
 * searches. Requested item is required for such cases because it contains values for the item key attributes.
 */
public class RequestedItemPresenceValidator implements ItemSearchRequestValidator
{
	/**
	 * {@inheritDoc}
	 *
	 * @throws MissingRequestedItemException if {@code request.getRequestedItem().isEmpty()}
	 */
	@Override
	public void validate(final ItemSearchRequest request)
	{
		if (request.getRequestedItem().isEmpty())
		{
			throw new MissingRequestedItemException(request);
		}
	}
}
