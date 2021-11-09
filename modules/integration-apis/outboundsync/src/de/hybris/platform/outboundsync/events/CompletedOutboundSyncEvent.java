/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.events;

import de.hybris.platform.core.PK;

import javax.annotation.concurrent.Immutable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Event triggered for every item, for which synchronization has been attempted during a cron job run.
 */
@Immutable
public final class CompletedOutboundSyncEvent extends OutboundSyncEvent
{
	private boolean success;
	private int changesCompleted;

	/**
	 * Instantiates this event.
	 * @param cronJobPk primary key of the job that processed the item synchronization
	 * @param success {@code true}, if synchronization was successful; {@code false} otherwise.
	 * @param changesCnt number of changes processed during the item synchronization.
	 */
	public CompletedOutboundSyncEvent(final PK cronJobPk, final boolean success, final int changesCnt)
	{
		super(cronJobPk);
		this.success = success;
		changesCompleted = changesCnt;
	}

	/**
	 * Determines whether the item was synchronized successfully or not.
	 * @return {@code true}, if synchronization was successful; {@code false} otherwise.
	 */
	public boolean isSuccess()
	{
		return success;
	}

	/**
	 * Retrieves number of changes processed for the item. It's possible that single item synchronizes multiple changes, which is
	 * especially true in cases when items related to the root integration object item change. For example, in case of multiple order
	 * entries changes, only one order entry item will be synchronized for all those changes.
	 * @return number of changes processed for the item.
	 */
	public int getChangesCompleted()
	{
		return changesCompleted;
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
			CompletedOutboundSyncEvent that = (CompletedOutboundSyncEvent) o;
			return getCronJobPk().equals(that.getCronJobPk())
					&& success == that.success
					&& changesCompleted == that.changesCompleted;
		}
		return false;

	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(17, 37)
				.append(getCronJobPk())
				.append(isSuccess())
				.append(getChangesCompleted())
				.toHashCode();
	}

	@Override
	public String toString()
	{
		return "CompletedOutboundSyncEvent{" +
				"cronJobPk=" + getCronJobPk() +
				", success=" + success +
				", changesCompleted=" + changesCompleted +
				'}';
	}
}
