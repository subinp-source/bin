/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundsync.job.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.outboundsync.dto.OutboundItem;
import de.hybris.platform.outboundsync.dto.OutboundItemDTO;
import de.hybris.platform.outboundsync.job.OutboundItemFactory;
import de.hybris.platform.outboundsync.model.OutboundChannelConfigurationModel;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.model.ModelService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of the {@link de.hybris.platform.outboundsync.job.OutboundItemFactory}
 */
public class DefaultOutboundItemFactory implements OutboundItemFactory
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultOutboundItemFactory.class);

	private ModelService modelService;

	@Override
	public OutboundItem createItem(final OutboundItemDTO itemDto)
	{
		final ItemModel changedItemModel = findItemByPk(itemDto.getItem().getPK());
		final IntegrationObjectModel integrationObject = findItemByPk(itemDto.getIntegrationObjectPK());
		final OutboundChannelConfigurationModel channelConfiguration = findItemByPk(itemDto.getChannelConfigurationPK());

		return OutboundItem.outboundItem()
				.withItemChange(itemDto.getItem())
				.withChangedItemModel(changedItemModel)
				.withIntegrationObject(integrationObject)
				.withChannelConfiguration(channelConfiguration)
				.build();
	}

	private <T extends ItemModel> T findItemByPk(final Long pk)
	{
		try
		{
			if (pk != null)
			{
				return getModelService().get(PK.fromLong(pk));
			}
		}
		catch (final ModelLoadingException e)
		{
			LOG.warn("The item with PK={} was not found.", pk, e);
		}
		return null;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService service)
	{
		modelService = service;
	}
}
