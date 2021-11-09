/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package de.hybris.platform.integrationservices.search;

import de.hybris.platform.integrationservices.item.IntegrationItem;

/**
 * Exception thrown when looking up for an item, and there is more than one item returned.
 */
public class NonUniqueItemFoundException extends RuntimeException
{
	private static final String MESSAGE_TEMPLATE = "Multiple items found when unique item of type '%s' in integration object '%s' is searched by its key '%s'";
	private final transient ItemSearchRequest itemSearchRequest;

	public NonUniqueItemFoundException(final ItemSearchRequest request)
	{
		this(request, message(request));
	}

	public NonUniqueItemFoundException(final String message)
	{
		this(null, message);
	}

	protected NonUniqueItemFoundException(final ItemSearchRequest request, final String message)
	{
		super(message);
		itemSearchRequest = request;
	}

	private static String message(final ItemSearchRequest request)
	{
		final var ioCode = request.getTypeDescriptor().getIntegrationObjectCode();
		final var ioItemCode = request.getTypeDescriptor().getItemCode();
		final var key = request.getRequestedItem().map(IntegrationItem::getIntegrationKey).orElse(null);
		return String.format(MESSAGE_TEMPLATE, ioItemCode, ioCode, key);
	}

	/**
	 * Returns request that resulted in multiple unique items returned.
	 *
	 * @return context request for unique item search that resulted in multiple items found.
	 */
	public ItemSearchRequest getRequest()
	{
		return itemSearchRequest;
	}
}
