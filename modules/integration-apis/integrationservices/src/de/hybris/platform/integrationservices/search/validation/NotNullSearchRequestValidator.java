/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.search.validation;

import de.hybris.platform.integrationservices.search.ItemSearchRequest;

/**
 * Verifies that search request is provided and is not {@code null}.
 */
public class NotNullSearchRequestValidator implements ItemSearchRequestValidator
{
	/**
	 * {@inheritDoc}
	 * @throws NullItemSearchRequestException if the request is {@code null}
	 */
	@Override
	public void validate(final ItemSearchRequest request)
	{
		if (request == null)
		{
			throw new NullItemSearchRequestException();
		}
	}
}
