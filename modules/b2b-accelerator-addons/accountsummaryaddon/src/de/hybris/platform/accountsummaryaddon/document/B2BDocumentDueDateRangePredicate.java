/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.accountsummaryaddon.document;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.time.DateUtils;

import de.hybris.platform.accountsummaryaddon.model.B2BDocumentModel;
import de.hybris.platform.accountsummaryaddon.utils.XDate;


/**
 * Predicate uses to filter all B2B Documents for a specific range of due dates.
 * 
 */
public class B2BDocumentDueDateRangePredicate implements Predicate
{

	private final NumberOfDayRange dateRange;

	public B2BDocumentDueDateRangePredicate(final NumberOfDayRange dateRange)
	{
		this.dateRange = dateRange;
	}

	@Override
	public boolean evaluate(final Object doc)
	{
		if (!(doc instanceof B2BDocumentModel))
		{
			return false;
		}

		final B2BDocumentModel document = (B2BDocumentModel) doc;

		final Date min = XDate.setToEndOfDay(DateUtils.addDays(new Date(), -dateRange.getMinBoundary().intValue()));

		if (document.getDueDate() == null || document.getDueDate().getTime() > min.getTime())
		{
			return false;
		}

		if (dateRange.getMaxBoundary() == null)
		{
			return true;
		}

		final Date max = DateUtils.truncate(DateUtils.addDays(new Date(), -dateRange.getMaxBoundary().intValue()),
				Calendar.DAY_OF_MONTH);

		return document.getDueDate().getTime() >= max.getTime();
	}
}
