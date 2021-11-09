/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence.validation;

/**
 * Exception thrown when trying to persist an instance of abstract type.
 */
public class InstanceCreationOfAbstractTypeException extends RuntimeException
{
	private final String integrationItemType;

	/**
	 * Instantiates this exception
	 *
	 * @param item code of the request integration object item
	 */
	public InstanceCreationOfAbstractTypeException(final String item)
	{
		super(message(item));
		integrationItemType = item;
	}

	private static String message(final String intItem)
	{
		return String.format("The type %s cannot be persisted because it is an abstract type.", intItem);
	}

	public String getIntegrationItemType()
	{
		return integrationItemType;
	}
}

