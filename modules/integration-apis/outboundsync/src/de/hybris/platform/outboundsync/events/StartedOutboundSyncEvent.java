/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.events;

import de.hybris.platform.core.PK;

import java.util.Date;

import javax.annotation.concurrent.Immutable;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.common.base.Preconditions;

/**
 * Event triggered when the Outbound Synchronization Cron Job has been started.
 */
@Immutable
public final class StartedOutboundSyncEvent extends OutboundSyncEvent
{
	private int itemCount;
	private Date startTime;

	/**
	 * Instantiates this event
	 *
	 * @param jobPk primary key of the outbound sync job, for which this event is fired.
	 * @param time  time when the job started.
	 * @param cnt   number of items being synchronized by the job.
	 */
	public StartedOutboundSyncEvent(@NotNull final PK jobPk, @NotNull final Date time, final int cnt)
	{
		super(jobPk);
		Preconditions.checkArgument(time != null, "Start time cannot be null");
		startTime = time;
		itemCount = cnt;
	}

	public int getItemCount()
	{
		return itemCount;
	}

	public @NotNull Date getStartTime()
	{
		return startTime;
	}

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}

		if (o != null && getClass() == o.getClass())
		{
			StartedOutboundSyncEvent that = (StartedOutboundSyncEvent) o;
			return new EqualsBuilder()
					.append(getCronJobPk(), that.getCronJobPk())
					.append(startTime, that.startTime)
					.append(itemCount, that.itemCount)
					.isEquals();
		}
		return false;

	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(17, 37)
				.append(getCronJobPk())
				.append(getStartTime())
				.append(getItemCount())
				.toHashCode();
	}

	@Override
	public String toString()
	{
		return "StartedOutboundSyncEvent{" +
				"cronJobPk=" + getCronJobPk() +
				", startTime=" + startTime +
				", itemCount=" + itemCount +
				'}';
	}
}
