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

package de.hybris.platform.integrationservices.service.impl;

import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.integrationservices.model.DescriptorFactory;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.integrationservices.service.IntegrationObjectService;
import de.hybris.platform.integrationservices.service.ItemTypeDescriptorService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of the {@code ItemTypeDescriptorService}
 */
public class DefaultItemTypeDescriptorService implements ItemTypeDescriptorService
{
	private static final Logger LOG = Log.getLogger(DefaultItemTypeDescriptorService.class);

	private IntegrationObjectService integrationObjectService;
	private DescriptorFactory descriptorFactory;

	@Override
	public Optional<TypeDescriptor> getTypeDescriptor(final String objCode, final String objItemCode)
	{
		return getIntegrationObjectService().findIntegrationObjectItem(objCode, objItemCode)
				.map(model -> getDescriptorFactory().createItemTypeDescriptor(model));
	}

	@Override
	public Optional<TypeDescriptor> getTypeDescriptorByTypeCode(final String objCode, final String itemTypeCode)
	{
		final IntegrationObjectItemModel model = getIoiModel(objCode, itemTypeCode);
		return Optional.ofNullable(model).map(m -> getDescriptorFactory().createItemTypeDescriptor(m));
	}

	private IntegrationObjectItemModel getIoiModel(final String objCode, final String itemTypeCode)
	{
		try
		{
			return getIntegrationObjectService().findIntegrationObjectItemByTypeCode(objCode, itemTypeCode);
		}
		catch (final ModelNotFoundException | UnknownIdentifierException e)
		{
			LOG.debug("The integrationObjectItem for typeCode {} could not be found. Looking up by parent types.", itemTypeCode);
			return findIntegrationObjectItemMatchingTypeCode(objCode, itemTypeCode);
		}
	}

	private IntegrationObjectItemModel findIntegrationObjectItemMatchingTypeCode(final String objCode, final String itemTypeCode)
	{
		try
		{
			return getIntegrationObjectService().findIntegrationObjectItemByParentTypeCode(objCode, itemTypeCode);
		}
		catch (final ModelNotFoundException | UnknownIdentifierException e)
		{
			LOG.debug("The fallback strategy search for typeCode {} could not find any model.", itemTypeCode);
			return null;
		}
	}

	protected IntegrationObjectService getIntegrationObjectService()
	{
		return integrationObjectService;
	}

	@Required
	public void setIntegrationObjectService(final IntegrationObjectService service)
	{
		integrationObjectService = service;
	}

	protected DescriptorFactory getDescriptorFactory()
	{
		return descriptorFactory;
	}

	@Required
	public void setDescriptorFactory(final DescriptorFactory factory)
	{
		descriptorFactory = factory;
	}
}
