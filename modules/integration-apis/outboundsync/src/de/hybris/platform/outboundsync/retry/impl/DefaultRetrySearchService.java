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
import de.hybris.platform.outboundsync.dto.OutboundItem;
import de.hybris.platform.outboundsync.dto.OutboundItemDTOGroup;
import de.hybris.platform.outboundsync.model.OutboundSyncRetryModel;
import de.hybris.platform.outboundsync.retry.RetrySearchService;
import de.hybris.platform.outboundsync.retry.SyncRetryNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation for searching for persisted retries based on the {@link OutboundItem}
 */
public class DefaultRetrySearchService implements RetrySearchService
{
	private static final Logger LOG = Log.getLogger(DefaultRetrySearchService.class);

	private FlexibleSearchService flexibleSearchService;

	@Override
	public OutboundSyncRetryModel findRetry(final OutboundItemDTOGroup outboundItemDTOGroup)
	{
		final OutboundSyncRetryModel sample = new OutboundSyncRetryModel();
		sample.setItemPk(outboundItemDTOGroup.getRootItemPk());
		sample.setChannel(outboundItemDTOGroup.getChannelConfiguration());

		try
		{
			return getFlexibleSearchService().getModelByExample(sample);
		}
		catch (final ModelNotFoundException e)
		{
			final RuntimeException exception = new SyncRetryNotFoundException(outboundItemDTOGroup.getRootItemPk(), outboundItemDTOGroup.getChannelConfiguration().getCode());
			LOG.debug(exception.getMessage());
			throw exception;
		}
	}

	protected FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}
}
