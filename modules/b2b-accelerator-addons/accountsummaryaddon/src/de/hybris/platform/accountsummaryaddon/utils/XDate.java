/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.accountsummaryaddon.utils;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;


/**
 * Used to provide Date utilities
 */
public final class XDate
{

	private XDate()
	{
		// No public constructor for utility class
	}

	public static Date setToEndOfDay(final Date date)
	{
		Date newDate = new Date(date.getTime());
		newDate = DateUtils.setHours(newDate, 23);
		newDate = DateUtils.setMinutes(newDate, 59);
		newDate = DateUtils.setSeconds(newDate, 59);
		newDate = DateUtils.setMilliseconds(newDate, 999);
		return newDate;
	}
}
