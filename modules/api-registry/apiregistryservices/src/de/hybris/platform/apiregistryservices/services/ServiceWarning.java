/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.services;

/**
 * Class encapsulating information of a service processing warning
 */
public class ServiceWarning<T>
{
	private final T object;
	private final String message;

	/**
	 * Default constructor of an immutable entity
	 * @param object related instance
	 * @param message warning message
	 */
	public ServiceWarning(final T object, final String message)
	{
		this.object = object;
		this.message = message;
	}

	/**
	 * Returns an instance related to the warning
	 */
	public T getObject()
	{
		return object;
	}

	/**
	 * Returns warning message
	 */
	public String getMessage()
	{
		return message;
	}
}
