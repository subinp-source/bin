/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.events;

import de.hybris.platform.core.PK;

import javax.annotation.concurrent.Immutable;
import javax.validation.constraints.NotNull;

/**
 * Event indicates that a single item change was not processed. This is because it happened to an item that is related to the
 * root item in the integration object but itself is not included in the integration object; or, if the outbound sync does not
 * process this kind of changes, e.g. DELETE.
 */
@Immutable
public final class IgnoredOutboundSyncEvent extends OutboundSyncEvent
{
	/**
	 * Instantiates this event
	 * @param cronJobPk primary key of the job, for which the change was ignored
	 *
	 */
	public IgnoredOutboundSyncEvent(@NotNull final PK cronJobPk)
	{
		super(cronJobPk);
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
			IgnoredOutboundSyncEvent that = (IgnoredOutboundSyncEvent) o;
			return getCronJobPk().equals(that.getCronJobPk());
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return getCronJobPk().hashCode();
	}

	@Override
	public String toString()
	{
		return "IgnoredOutboundSyncEvent{" +
				"cronJobPk=" + getCronJobPk() +
				'}';
	}
}
