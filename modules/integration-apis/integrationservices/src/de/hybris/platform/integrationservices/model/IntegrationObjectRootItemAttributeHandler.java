/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model;

import de.hybris.platform.integrationservices.validator.SingleRootItemConstraintViolationException;
import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler;

import java.util.List;
import java.util.stream.Collectors;

public class IntegrationObjectRootItemAttributeHandler extends AbstractDynamicAttributeHandler<IntegrationObjectItemModel, IntegrationObjectModel>
{
	@Override
	public IntegrationObjectItemModel get(final IntegrationObjectModel integrationObject)
	{
		final List<IntegrationObjectItemModel> rootItems = integrationObject.getItems()
				.stream()
				.filter(IntegrationObjectItemModel::getRoot)
				.collect(Collectors.toList());

		if(rootItems.size() > 1)
		{
			throw new SingleRootItemConstraintViolationException(integrationObject.getCode(), rootItems);
		}

		return rootItems.isEmpty() ? null : rootItems.get(0);
	}
}
