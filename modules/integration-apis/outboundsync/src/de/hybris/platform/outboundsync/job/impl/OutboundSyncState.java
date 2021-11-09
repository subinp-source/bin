/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.job.impl;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.Date;
import java.util.OptionalInt;

import javax.annotation.concurrent.Immutable;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.common.base.Preconditions;

/**
 * Represents state of a single outbound sync cron job run.
 */
@Immutable
public final class OutboundSyncState
{
	private final Integer totalItemsRequested;
	private final int successCount;
	private final int errorCount;
	private final int unprocessedCount;
	private final Date startTime;
	private final Date endTime;
	private final CronJobStatus cronJobStatus;
	private final CronJobResult cronJobResult;
	private final boolean systemicError;
	private final boolean aborted;

	private OutboundSyncState(Integer allItems, Date started, int successes, int errors, int unprocessed, Date ended,
	                          boolean abort, boolean systemError)
	{
		Preconditions.checkArgument(allItems == null || allItems >= 0, "Total items count cannot be negative");
		Preconditions.checkArgument(successes >= 0, "Number of successfully processed items cannot be negative");
		Preconditions.checkArgument(errors >= 0, "Number of errors cannot be negative");
		Preconditions.checkArgument(unprocessed >= 0, "Number of items ignored after abort cannot be negative");

		totalItemsRequested = allItems;
		startTime = started;
		successCount = successes;
		errorCount = errors;
		unprocessedCount = unprocessed;
		aborted = abort;
		systemicError = systemError;
		cronJobStatus = deriveCronJobStatus();
		cronJobResult = deriveCronJobResult();
		endTime = ended != null ? ended : deriveEndTime();
	}

	private Date deriveEndTime()
	{
		return isAllItemsProcessed() ? new Date() : null;
	}

	private CronJobStatus deriveCronJobStatus()
	{
		return isAllItemsProcessed()
				? finishedJobStatus()
				: CronJobStatus.RUNNING;
	}

	private CronJobStatus finishedJobStatus()
	{
		return isAborted() ? CronJobStatus.ABORTED : CronJobStatus.FINISHED;
	}

	@NotNull
	private CronJobResult deriveCronJobResult()
	{
		if (isSystemicError())
		{
			return CronJobResult.FAILURE;
		}
		if (errorCount > 0)
		{
			return CronJobResult.ERROR;
		}
		return successCount > 0 || getTotalItemsRequested().orElse(-1) == 0
				? CronJobResult.SUCCESS : CronJobResult.UNKNOWN;
	}

	/**
	 * Determines whether all changes detected by the outbound sync job have been processed or not.
	 * @return {@code true}, if all changes are processed; {@code false}, if at least one change remains queued and not processed.
	 */
	public boolean isAllItemsProcessed()
	{
		final OptionalInt totalCnt = getTotalItemsRequested();
		return totalCnt.isPresent() && successCount + errorCount + unprocessedCount >= totalCnt.getAsInt();
	}

	/**
	 * Determines whether the outbound sync job is aborted
	 * @return {@code true}, if outbound sync job is aborted; else {@code false}
	 */
	public boolean isAborted()
	{
		return aborted;
	}

	/**
	 * Determines whether there is a systemic error which cannot be recovered
	 * @return {@code true}, if a systemic error exists; else {@code false}
	 */
	public boolean isSystemicError()
	{
		return systemicError;
	}

	/**
	 * Retrieves total number of changes to by synchronized to an external system.
	 *
	 * @return {@code Optional} containing total number of changes, or an empty {@code Optional}, if the total number of changes is
	 * not known yet.
	 */
	public @NotNull OptionalInt getTotalItemsRequested()
	{
		return totalItemsRequested == null
				? OptionalInt.empty()
				: OptionalInt.of(totalItemsRequested);
	}

	/**
	 * Retrieves number of changes reported by the delta detect that synchronized successfully.
	 *
	 * @return number of successfully synchronized changes or 0 when all items failed to synchronize or the job is aborted before
	 * any item synchronized successfully.
	 */
	public int getSuccessCount()
	{
		return successCount;
	}

	/**
	 * Retrieves number of changes reported by the delta detect that failed to synchronize.
	 *
	 * @return number of failed to synchronize changes or 0, if all items synchronized successfully or the job was aborted before
	 * any item failed to synchronize.
	 */
	public int getErrorCount()
	{
		return errorCount;
	}

	/**
	 * Retrieves number of changes, reported by the delta detect, ignored after the job was aborted.
	 *
	 * @return number of changes ignored or 0, if the job was not aborted.
	 */
	public int getUnprocessedCount()
	{
		return unprocessedCount;
	}

	/**
	 * Retrieves the outbound sync job start time
	 *
	 * @return time when the job started.
	 */
	public @NotNull Date getStartTime()
	{
		return startTime;
	}

	/**
	 * Retrieves the outbound sync job end time.
	 *
	 * @return time when the job finished, i.e. all changes are processed. This value may be {@code null} while the job is still
	 * running.
	 */
	public Date getEndTime()
	{
		return endTime;
	}

	/**
	 * Retrieves current status of the outbound sync job.
	 *
	 * @return current status of the job
	 */
	public @NotNull CronJobStatus getCronJobStatus()
	{
		return cronJobStatus;
	}

	/**
	 * Retrieves current result of the outbound sync job.
	 *
	 * @return current result of the job. The result is {@link CronJobResult#UNKNOWN} while the
	 * job is still running. Once the job is finished or aborted, the result becomes either {@link CronJobResult#ERROR} or
	 * {@link CronJobResult#SUCCESS} depending on whether errors are present or not, i.e. {@code getErrorCount() > 0}.
	 */
	public @NotNull CronJobResult getCronJobResult()
	{
		return cronJobResult;
	}

	/**
	 * Represents this state as {@link PerformResult}
	 * @return this state as {@code PerformResult}
	 */
	public @NotNull PerformResult asPerformResult()
	{
		return new PerformResult(cronJobResult, cronJobStatus);
	}

	@Override
	public String toString()
	{
		return "OutboundSyncState{" +
				"startTime=" + startTime +
				", cronJobStatus=" + cronJobStatus +
				", cronJobResult=" + cronJobResult +
				", successCount=" + successCount +
				", errorCount=" + errorCount +
				", unprocessedCount=" + unprocessedCount +
				(totalItemsRequested != null ? (", totalItemsRequested=" + totalItemsRequested) : "") +
				", endTime=" + endTime +
				", aborted=" + aborted +
				", systemicError=" + systemicError +
				'}';
	}

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		final OutboundSyncState that = (OutboundSyncState) o;
		return new EqualsBuilder()
				.append(totalItemsRequested, that.totalItemsRequested)
				.append(successCount, that.successCount)
				.append(errorCount, that.errorCount)
				.append(unprocessedCount, that.unprocessedCount)
				.append(startTime, that.startTime)
				.append(endTime, that.endTime)
				.append(aborted, that.aborted)
				.append(systemicError, that.systemicError)
				.isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(17, 37)
				.append(totalItemsRequested)
				.append(successCount)
				.append(errorCount)
				.append(unprocessedCount)
				.append(startTime)
				.append(endTime)
				.append(aborted)
				.append(systemicError)
				.toHashCode();
	}

	/**
	 * Determines whether the job state changed beyond just a count change.
	 * @param prevState a previous state to compare with
	 * @return {@code true}, if the job status, the job result or number of total items to process changed;
	 * {@code false}, if these states are identical or only already changed count got incremented.
	 */
	public boolean isChangedFrom(final OutboundSyncState prevState)
	{
		return ! new EqualsBuilder()
				.append(totalItemsRequested, prevState.totalItemsRequested)
				.append(cronJobStatus, prevState.cronJobStatus)
				.append(cronJobResult, prevState.cronJobResult)
				.isEquals();
	}

	/**
	 * A builder for creating immutable {@link OutboundSyncState}
	 */
	public static final class Builder
	{
		private Integer totalItemsRequested;
		private int successCount;
		private int errorCount;
		private int unprocessedCount;
		private Date startTime;
		private Date endTime;
		private boolean aborted;
		private boolean systemicError;

		private Builder()
		{
			// non-instantiable
		}

		/**
		 * Creates initial outbound sync job state builder instance.
		 *
		 * @return new builder instance, which is initialized with the provided total number of items to be processed by the job
		 * and job start date, which is set to the current time.
		 */
		public static Builder initialOutboundSyncState()
		{
			return new Builder();
		}

		/**
		 * Creates a new instance of this builder by initializing its state to the provided outbound sync state. Modifiable
		 * attributes can be then changed in the builder, but those attributes that never change will be carried over into the
		 * states created by this this builder.
		 *
		 * @param initialState another state to initialize this builder to
		 * @return a new builder instance initialized to the provided state
		 */
		public static Builder from(@NotNull final OutboundSyncState initialState)
		{
			Preconditions.checkArgument(initialState != null, "The state cannot be null");
			final Integer totalCount = initialState.getTotalItemsRequested().isPresent()
					? initialState.getTotalItemsRequested().getAsInt()
					: null;
			return new Builder()
					.withTotalItems(totalCount)
					.withStartTime(initialState.getStartTime())
					.withEndTime(initialState.getEndTime())
					.withErrorCount(initialState.getErrorCount())
					.withSuccessCount(initialState.getSuccessCount())
					.withUnprocessedCount(initialState.getUnprocessedCount())
					.withAborted(initialState.isAborted())
					.withSystemicError(initialState.isSystemicError());
		}

		public Builder withTotalItems(Integer cnt)
		{
			if (totalItemsRequested == null)
			{
				totalItemsRequested = cnt;
			}
			return this;
		}

		public Builder withSuccessCount(int successCount)
		{
			this.successCount = successCount;
			return this;
		}

		public Builder withErrorCount(int errorCount)
		{
			this.errorCount = errorCount;
			return this;
		}

		public Builder withUnprocessedCount(int unprocessedCount)
		{
			this.unprocessedCount = unprocessedCount;
			return this;
		}

		public Builder withStartTime(Date time)
		{
			startTime = time;
			return this;
		}

		Builder withEndTime(Date endTime)
		{
			this.endTime = endTime;
			return this;
		}

		public Builder withAborted(boolean aborted)
		{
			this.aborted = aborted;
			return this;
		}

		public Builder withSystemicError(boolean systemicError)
		{
			this.systemicError = systemicError;
			return this;
		}

		public OutboundSyncState build()
		{
			return new OutboundSyncState(totalItemsRequested, startTime, successCount, errorCount, unprocessedCount, endTime, aborted, systemicError);
		}
	}
}
