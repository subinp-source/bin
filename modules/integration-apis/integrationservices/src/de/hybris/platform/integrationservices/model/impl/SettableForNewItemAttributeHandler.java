/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import static de.hybris.platform.integrationservices.model.impl.SettableUtils.isInitial;
import static de.hybris.platform.integrationservices.model.impl.SettableUtils.isWritable;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.integrationservices.model.SettableAttributeHandler;
import de.hybris.platform.servicelayer.model.ModelService;

class SettableForNewItemAttributeHandler implements SettableAttributeHandler
{
	private final ModelService modelService;

	public SettableForNewItemAttributeHandler(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Override
	public boolean isApplicable(final ItemModel item)
	{
		return modelService.isNew(item);
	}

	@Override
	public boolean isSettable(final AttributeDescriptorModel descriptor)
	{
		return isInitial(descriptor) || isWritable(descriptor);
	}
}
