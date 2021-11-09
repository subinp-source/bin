/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.search;

/**
 * A common exception for any problem with the order by condition used in the item search.
 */
public class OrderByNotSupportedException extends ItemSearchException
{
	/**
	 * Instantiates this exception.
	 * @param msg a message describing the problem
	 */
	public OrderByNotSupportedException(final String msg)
	{
		super(msg);
	}
}
