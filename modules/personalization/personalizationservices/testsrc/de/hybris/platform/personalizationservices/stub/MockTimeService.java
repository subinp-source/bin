/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationservices.stub;

import de.hybris.platform.servicelayer.time.TimeService;

import java.util.Date;


public class MockTimeService implements TimeService
{
	private Date time;

	public MockTimeService()
	{
		setCurrentTime(new Date(0L));
	}

	public MockTimeService(final Date instant)
	{
		setCurrentTime(instant);
	}

	@Override
	public void setCurrentTime(final Date instant)
	{
		this.time = instant;
	}

	@Override
	public Date getCurrentTime()
	{
		return time;
	}

	@Override
	public long getTimeOffset()
	{
		return 0;
	}
	
	@Override
	public void resetTimeOffset()
	{
		//noop
	}

	@Override
	public void setTimeOffset(final long timeOffsetMillis)
	{
		//noop
	}

	@Override
	public Date getCurrentDateWithTimeNormalized() {
		return time;
	}
}