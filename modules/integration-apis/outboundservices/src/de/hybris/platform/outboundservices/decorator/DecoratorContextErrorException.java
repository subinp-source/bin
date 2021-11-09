/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.decorator;

import java.util.Collection;

/**
 * Exception thrown when errors are present in the {@link DecoratorContext}
 */
public class DecoratorContextErrorException extends RuntimeException
{
	private static final String ERROR_MESSAGE = "Errors found in the DecoratorContext: '%s'";
	private final transient DecoratorContext context;

	/**
	 * Constructor for the exception.
	 *
	 * @param context which contains the errors.
	 */
	public DecoratorContextErrorException(final DecoratorContext context)
	{
		this.context = context;
	}

	/**
	 * Provides access to the context that generated the exception.
	 *
	 * @return context containing errors.
	 */
	public DecoratorContext getContext()
	{
		return context;
	}

	/**
	 * Retrieves errors, which prevented outbound request from processing.
	 *
	 * @return errors associated with the decorator context.
	 */
	public Collection<String> getErrors()
	{
		return context.getErrors();
	}

	@Override
	public String getMessage()
	{
		return String.format(ERROR_MESSAGE, context.getErrors().toString());
	}
}
