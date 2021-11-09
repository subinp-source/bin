/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.search.validation;

import de.hybris.platform.integrationservices.search.ItemSearchRequest;

/**
 * An exception indicating that {@link de.hybris.platform.integrationservices.search.ItemSearchRequest} is not complete,
 * or contains contradicting conditions, or whatever else it might be, and therefore the item search was not performed.
 */
public class ItemSearchRequestValidationException extends RuntimeException
{
	private static final String DEFAULT_MESSAGE = "Invalid request: ";

	private final transient ItemSearchRequest rejectedRequest;

	/**
	 * Instantiates this exception
	 *
	 * @param request request that did not pass validation
	 */
	public ItemSearchRequestValidationException(final ItemSearchRequest request)
	{
		this(request, DEFAULT_MESSAGE + request);
	}

	/**
	 * Instantiates this exception with a provided message explaining the validation error.
	 *
	 * @param request a request that failed validation
	 * @param message message explaining the validation problem
	 */
	public ItemSearchRequestValidationException(final ItemSearchRequest request, final String message)
	{
		super(message);
		rejectedRequest = request;
	}

	/**
	 * Retrieves item search request that failed validation.
	 *
	 * @return request that was rejected for search by a {@link ItemSearchRequestValidator}
	 */
	public ItemSearchRequest getRejectedRequest()
	{
		return rejectedRequest;
	}
}
