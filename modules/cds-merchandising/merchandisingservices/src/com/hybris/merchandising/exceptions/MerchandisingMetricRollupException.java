/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.exceptions;

/**
 * {@link Exception} thrown when there was a problem trying to calculate the reporting group to roll product variants
 * up to
 */
public class MerchandisingMetricRollupException extends Exception
{
	public MerchandisingMetricRollupException(final String message)
	{
		super(message);
	}
}
