/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.inboundservices.persistence.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.inboundservices.persistence.ItemModelFactory;
import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.servicelayer.model.ModelService;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultItemModelFactory implements ItemModelFactory
{
	private static final Logger LOG = Log.getLogger(DefaultItemModelFactory.class);

	private ModelService modelService;

	/**
	 * {@inheritDoc}
	 * @throws de.hybris.platform.servicelayer.exceptions.SystemException if new instance could not be created
	 */
	@Override
	public ItemModel createItem(final PersistenceContext context)
	{
		final TypeDescriptor itemType = context.getIntegrationItem().getItemType();
		LOG.trace("Create a new {} model instance", itemType.getItemCode());

		return getModelService().create(itemType.getTypeCode());
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
