/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package de.hybris.platform.odata2services.odata.persistence.creation;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.inboundservices.persistence.ItemModelFactory;
import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.integrationservices.item.IntegrationItem;
import de.hybris.platform.integrationservices.service.IntegrationObjectService;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.odata2services.odata.persistence.InternalProcessingException;
import de.hybris.platform.servicelayer.exceptions.ModelCreationException;
import de.hybris.platform.servicelayer.exceptions.ModelInitializationException;
import de.hybris.platform.servicelayer.model.ModelService;

import org.slf4j.Logger;

/**
 * @deprecated use {@link ItemModelFactory#createItem(PersistenceContext)}
 */
@Deprecated(since = "1905.07-CEP", forRemoval = true)
public class AlwaysCreateItemStrategy implements CreateItemStrategy
{
	private static final Logger LOG = Log.getLogger(AlwaysCreateItemStrategy.class);

	private ModelService modelService;
	private IntegrationObjectService integrationObjectService;
	private ItemModelFactory itemFactory;

	@Override
	public ItemModel createItem(final PersistenceContext ctx)
	{
		final IntegrationItem item = ctx.getIntegrationItem();
		final String entityName = item.getItemType().getItemCode();
		LOG.trace("Item '{}' -> '{}' does not exist, trying to create a new one.", entityName, item);
		try
		{
			return itemFactory.createItem(ctx);
		}
		catch (final ModelCreationException | ModelInitializationException e)
		{
			LOG.trace("internal_error due to exception: ", e);
			throw new InternalProcessingException(e);
		}
	}

	/**
	 * Not used since 1905.07-CEP
	 */
	protected ModelService getModelService()
	{
		return this.modelService;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * Not used since 1905.07-CEP
	 */
	protected IntegrationObjectService getIntegrationObjectService()
	{
		return this.integrationObjectService;
	}

	public void setIntegrationObjectService(final IntegrationObjectService integrationObjectService)
	{
		this.integrationObjectService = integrationObjectService;
	}

	public void setItemFactory(final ItemModelFactory factory)
	{
		itemFactory = factory;
	}
}
