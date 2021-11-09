/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.service.impl;

import de.hybris.platform.cmsfacades.common.service.TimeDiffService;
import de.hybris.platform.servicelayer.time.TimeService;

import java.util.Date;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link TimeDiffService}.
 */
public class DefaultTimeDiffService implements TimeDiffService
{
	private TimeService timeService;

	@Override
	public Long difference(final Date time)
	{
		return getTimeService().getCurrentTime().getTime() - time.getTime();
	}

	protected TimeService getTimeService()
	{
		return timeService;
	}

	@Required
	public void setTimeService(final TimeService timeService)
	{
		this.timeService = timeService;
	}
}
