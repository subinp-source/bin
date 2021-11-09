/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.accountsummaryaddon.document.service;

import java.util.List;

import de.hybris.platform.accountsummaryaddon.document.NumberOfDayRange;

/**
 * Provides services for Past Due Balance Date Range.
 *
 */
public interface PastDueBalanceDateRangeService
{
	/**
	 * Gets a list of number of days ranges.
	 * 
	 * @return date range list
	 */
	List<NumberOfDayRange> getNumberOfDayRange();
}
