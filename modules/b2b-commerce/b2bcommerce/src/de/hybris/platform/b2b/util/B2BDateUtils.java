/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.util;

import de.hybris.platform.b2b.enums.B2BPeriodRange;
import de.hybris.platform.util.StandardDateRange;

import java.util.Calendar;


public final class B2BDateUtils
{
	public StandardDateRange createDateRange(final B2BPeriodRange range)
	{
		TimeRange timeRange = new DayRange();
		switch (range)
		{
			case DAY:
				timeRange = new DayRange();
				break;
			case WEEK:
				timeRange = new WeekRange();
				break;
			case MONTH:
				timeRange = new MonthRange();
				break;
			case QUARTER:
				timeRange = new QuarterRange();
				break;
			case YEAR:
				timeRange = new YearRange();
				break;
		}
		return new StandardDateRange(timeRange.getStartOfRange(Calendar.getInstance()).getTime(), timeRange.getEndOfRange(
				Calendar.getInstance()).getTime());

	}

}
