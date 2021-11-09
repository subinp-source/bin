/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.accountsummaryaddon.document;

import java.util.Date;

public class DateRange implements Range
{

	private final Date minDateRange;
	private final Date maxDateRange;

	public DateRange(final Date minDateRange, final Date maxDateRange)
	{
		this.minDateRange = minDateRange;
		this.maxDateRange = maxDateRange;
	}


	@Override
	public Date getMinBoundary()
	{
		return this.minDateRange;
	}


	@Override
	public Date getMaxBoundary()
	{
		return this.maxDateRange;
	}
}
