/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.search;


/**
 * An exception indicating that a filtering condition used for the item search is not supported.
 */
public class FilterNotSupportedException extends ItemSearchException
{
	public FilterNotSupportedException(final String message)
	{
		super(message);
	}
}