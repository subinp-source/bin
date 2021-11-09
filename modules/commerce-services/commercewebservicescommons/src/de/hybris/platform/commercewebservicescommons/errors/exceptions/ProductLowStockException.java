/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.errors.exceptions;


/**
 * Thrown when there is insufficient stock at a warehouse.
 */
public class ProductLowStockException extends LowStockException
{
	private static final String SUBJECT_TYPE = "product";

	public ProductLowStockException(final String message)
	{
		super(message);
	}

	public ProductLowStockException(final String message, final String reason)
	{
		super(message, reason);
	}

	public ProductLowStockException(final String message, final String reason, final Throwable cause)
	{
		super(message, reason, cause);
	}

	public ProductLowStockException(final String message, final String reason, final String subject)
	{
		super(message, reason, subject);
	}

	public ProductLowStockException(final String message, final String reason, final String subject, final Throwable cause)
	{
		super(message, reason, subject, cause);
	}

	@Override
	public String getSubjectType()
	{
		return SUBJECT_TYPE;
	}
}
