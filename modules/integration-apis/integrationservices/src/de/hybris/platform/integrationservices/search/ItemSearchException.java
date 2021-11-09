/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.search;

/**
 * Indicates any problem with the item search.
 */
public class ItemSearchException extends RuntimeException
{
	/**
	 * Instantiates this exception.
	 * @param msg a message explaining the problem
	 */
	public ItemSearchException(final String msg)
	{
		super(msg);
	}
}
