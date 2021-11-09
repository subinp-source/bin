/*
 * [y] hybris Platform
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.outboundsync.retry.impl;

import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.outboundsync.config.impl.OutboundSyncConfiguration;
import de.hybris.platform.outboundsync.dto.OutboundItem;
import de.hybris.platform.outboundsync.dto.OutboundItemDTOGroup;
import de.hybris.platform.outboundsync.model.OutboundSyncRetryModel;
import de.hybris.platform.outboundsync.retry.RetrySearchService;
import de.hybris.platform.outboundsync.retry.RetryUpdateException;
import de.hybris.platform.outboundsync.retry.SyncRetryNotFoundException;
import de.hybris.platform.outboundsync.retry.SyncRetryService;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation for updating the persisted retries based on the results of the synchronization process
 */
public class DefaultSyncRetryService implements SyncRetryService
{
	private static final Logger LOG = Log.getLogger(DefaultSyncRetryService.class);

	private ModelService modelService;
	private OutboundSyncConfiguration outboundSyncConfiguration;
	private RetrySearchService retrySearchService;

	@Override
	public boolean handleSyncFailure(final OutboundItemDTOGroup dtoGroup)
	{
		return determineLastAttemptAndUpdateRetry(dtoGroup);
	}

	@Override
	public boolean determineLastAttemptAndUpdateRetry(final OutboundItemDTOGroup outboundItemDTOGroup)
	{
		return deriveRetry(outboundItemDTOGroup)
				.map(this::changeForUnsuccessfulSyncAttempt)
				.map(retry -> retry.getRemainingSyncAttempts() <= 0)
				.orElse(true);
	}

	/**
	 * Updates retry state and persists it.
	 * @param retry a retry record for the failed synchronization attempt.
	 * @return update retry model
	 */
	protected OutboundSyncRetryModel changeForUnsuccessfulSyncAttempt(final OutboundSyncRetryModel retry)
	{
		markRetryAsMaxRetriesReached(retry);
		return retry;
	}

	/**
	 * Increments the retry attempt count by 1, and persists the update
	 *
	 * @param retry {@link OutboundSyncRetryModel} to update
	 * @deprecated since 1905.08-CEP the implementation always uses {@link #changeForUnsuccessfulSyncAttempt(OutboundSyncRetryModel)}
	 */
	@Deprecated(since = "1905.08-CEP", forRemoval = true)
	protected void incrementRetryAttempt(final OutboundSyncRetryModel retry)
	{
		final int remaining = getOutboundSyncConfiguration().getMaxOutboundSyncRetries() - retry.getSyncAttempts();
		retry.setRemainingSyncAttempts(remaining < 0 ? 0 : remaining);
		retry.setSyncAttempts(retry.getSyncAttempts() + 1);
		retry.setReachedMaxRetries(retry.getRemainingSyncAttempts() <= 0);
		updateRetry(retry);
	}

	/**
	 * Sets the {@link OutboundSyncRetryModel#setReachedMaxRetries(Boolean)} to true,
	 * and persists the update.
	 *
	 * @param retry {@link OutboundSyncRetryModel} to update
	 * @deprecated since 1905.08-CEP the implementation always uses {@link #changeForUnsuccessfulSyncAttempt(OutboundSyncRetryModel)}
	 */
	@Deprecated(since = "1905.08-CEP", forRemoval = true)
	protected void markRetryAsMaxRetriesReached(final OutboundSyncRetryModel retry)
	{
		incrementRetryAttempt(retry);
	}

	/**
	 * Persists the given {@link OutboundSyncRetryModel}
	 *
	 * @param retry {@link OutboundSyncRetryModel} to save
	 */
	protected void updateRetry(final OutboundSyncRetryModel retry)
	{
		try
		{
			getModelService().save(retry);
		}
		catch (final ModelSavingException e)
		{
			LOG.trace("The Retry was not updated", e);
			throw new RetryUpdateException(retry);
		}
	}

	@Override
	public void handleSyncSuccess(final OutboundItemDTOGroup outboundItemDTOGroup)
	{
		try
		{
			final OutboundSyncRetryModel retry = findRetry(outboundItemDTOGroup);
			deleteRetry(retry);
		}
		catch (final SyncRetryNotFoundException e)
		{
			LOG.trace("Retry not found", e);
		}
	}

	private void deleteRetry(final OutboundSyncRetryModel retry)
	{
		try
		{
			getModelService().remove(retry);
		}
		catch (final ModelRemovalException e)
		{
			LOG.trace("The Retry was not removed", e);
			throw new RetryUpdateException(retry);
		}
	}

	private Optional<OutboundSyncRetryModel> deriveRetry(final OutboundItemDTOGroup dtoGroup)
	{
		try
		{
			return Optional.of(findRetry(dtoGroup));
		}
		catch (final SyncRetryNotFoundException e)
		{
			LOG.trace("Retry not found", e);
			return isRetriesEnabled()
					? Optional.of(createNewRetry(dtoGroup))
					: Optional.empty();
		}
	}

	private boolean isRetriesEnabled()
	{
		return getOutboundSyncConfiguration().getMaxOutboundSyncRetries() > 0;
	}

	/**
	 * Creates a new instance of the retry record but does not persist it.
	 *
	 * @param outboundItemDTOGroup {@link OutboundItemDTOGroup} consists the information for creating the {@link OutboundSyncRetryModel}
	 */
	protected OutboundSyncRetryModel createNewRetry(final OutboundItemDTOGroup outboundItemDTOGroup)
	{
		final OutboundSyncRetryModel retry = getModelService().create(OutboundSyncRetryModel.class);
		retry.setItemPk(outboundItemDTOGroup.getRootItemPk());
		retry.setChannel(outboundItemDTOGroup.getChannelConfiguration());
		retry.setSyncAttempts(0);
		return retry;
	}
	
	/**
	 * Searches the database for a {@link OutboundSyncRetryModel} that contains the information in the {@link OutboundItem}
	 *
	 * @param outboundItemDTOGroup {@link OutboundItemDTOGroup} consists the information for the search
	 * @return An OutboundSyncRetryModel if found
	 */
	protected OutboundSyncRetryModel findRetry(final OutboundItemDTOGroup outboundItemDTOGroup)
	{
		return getRetrySearchService().findRetry(outboundItemDTOGroup);
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected OutboundSyncConfiguration getOutboundSyncConfiguration()
	{
		return outboundSyncConfiguration;
	}

	@Required
	public void setOutboundSyncConfiguration(final OutboundSyncConfiguration outboundSyncConfiguration)
	{
		this.outboundSyncConfiguration = outboundSyncConfiguration;
	}

	protected RetrySearchService getRetrySearchService()
	{
		return retrySearchService;
	}

	@Required
	public void setRetrySearchService(final RetrySearchService retrySearchService)
	{
		this.retrySearchService = retrySearchService;
	}
}
