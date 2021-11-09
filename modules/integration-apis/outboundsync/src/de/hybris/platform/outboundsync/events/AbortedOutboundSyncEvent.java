/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.events;

import de.hybris.platform.core.PK;

import javax.annotation.concurrent.Immutable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Event is triggered for every item being processed after the job is aborted. It indicates that an item was picked up off the queue
 * but was ignored and was not attempted to synchronize. Before the job is aborted {@link CompletedOutboundSyncEvent} is fired
 * for every item processed.
 */
@Immutable
public final class AbortedOutboundSyncEvent extends OutboundSyncEvent
{
	private final int changesProcessed;

	/**
	 * Instantiates this event
	 * @param cronJobPk primary key of the job that was aborted.
	 * @param changesCnt number of changes processed by the job for the context item ignored during synchronization.
	 */
	public AbortedOutboundSyncEvent(final PK cronJobPk, final int changesCnt)
	{
		super(cronJobPk);
		changesProcessed = changesCnt;
	}

	/**
	 * Retrieves number of changes related to the item ignored.
	 * @return number of changes related to the item ignored. At a minimum this value is expected to be 1, but the number can be
	 * greater when multiple items related to the item being synchronized changed. For example, if Customer is
	 * being synchronized because two of its Addresses changed, this value will be 2.
	 */
	public int getChangesProcessed()
	{
		return changesProcessed;
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
			AbortedOutboundSyncEvent that = (AbortedOutboundSyncEvent) o;
			return getCronJobPk().equals(that.getCronJobPk())
					&& getChangesProcessed() == that.getChangesProcessed();
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(17, 37)
				.append(getCronJobPk())
				.append(getChangesProcessed())
				.toHashCode();
	}

	@Override
	public String toString()
	{
		return "AbortedOutboundSyncEvent{" +
				"cronJobPk=" + getCronJobPk() +
				", itemsProcessed=" + getChangesProcessed() +
				'}';
	}
}
