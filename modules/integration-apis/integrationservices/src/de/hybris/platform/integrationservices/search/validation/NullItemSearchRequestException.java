/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.search.validation;

import de.hybris.platform.integrationservices.search.ItemSearchRequest;

/**
 * Indicates that a {@code null} {@link de.hybris.platform.integrationservices.search.ItemSearchRequest}
 */
public class NullItemSearchRequestException extends ItemSearchRequestValidationException
{
	public NullItemSearchRequestException()
	{
		super(null, "ItemSearchRequest cannot be null for searching items");
	}
}
