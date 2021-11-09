/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservices.core.exceptions;

/**
 * Specific exception that is thrown when delivery address is not supported for the current session cart
 */
public class UnsupportedDeliveryAddressException extends Exception
{

	private final String addressId;

	/**
	 * @param id
	 */
	public UnsupportedDeliveryAddressException(final String id)
	{
		super("Address [" + id + "] is not supported for the current cart");
		this.addressId = id;
	}

	/**
	 * @return the addressId
	 */
	public String getAddressId()
	{
		return addressId;
	}
}
