/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.accountsummaryaddon.document;

public class NumberOfDayRange implements Range
{

	private final Integer minNumberOfDayRange;
	private final Integer maxNumberOfDayRange;

	public NumberOfDayRange(final Integer minNumberOfDayRange, final Integer maxNumberOfDayRange)
	{
		this.minNumberOfDayRange = minNumberOfDayRange;
		this.maxNumberOfDayRange = maxNumberOfDayRange;
	}

	@Override
	public Integer getMinBoundary()
	{
		return this.minNumberOfDayRange;
	}


	@Override
	public Integer getMaxBoundary()
	{
		return this.maxNumberOfDayRange;
	}
}
