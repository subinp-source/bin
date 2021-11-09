/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.search.validation;

import de.hybris.platform.integrationservices.search.ItemSearchRequest;

/**
 * Exception thrown when request for search a unique item does not contain requested item specification, i.e.
 * {@link ItemSearchRequest#getRequestedItem()} returns {@code Optional.empty()}.
 */
public class MissingRequestedItemException extends ItemSearchRequestValidationException
{
	private static final String MESSAGE_TEMPLATE = "Key is not specified for a unique item search: %s";

	public MissingRequestedItemException(final ItemSearchRequest request)
	{
		this(request, message(request));
	}

	protected MissingRequestedItemException(final ItemSearchRequest request, final String message)
	{
		super(request, message);
	}

	private static String message(final ItemSearchRequest request)
	{
		return String.format(MESSAGE_TEMPLATE, request);
	}
}
