/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorfacades.cart.action.exceptions;

import de.hybris.platform.acceleratorfacades.cart.action.CartEntryActionHandler;


/**
 * A general exception used by {@link CartEntryActionHandler#handleAction(long)} when an error occurs.
 */
public class CartEntryActionException extends Exception
{
	public CartEntryActionException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public CartEntryActionException(final String message)
	{
		super(message);
	}
}
