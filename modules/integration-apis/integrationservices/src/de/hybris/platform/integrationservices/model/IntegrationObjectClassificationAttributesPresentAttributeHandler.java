/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model;

import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler;

import org.apache.commons.collections4.CollectionUtils;

public class IntegrationObjectClassificationAttributesPresentAttributeHandler extends AbstractDynamicAttributeHandler<Boolean, IntegrationObjectModel>
{
	@Override
	public Boolean get(final IntegrationObjectModel integrationObject)
	{
		return integrationObject.getItems().stream()
				.map(IntegrationObjectItemModel::getClassificationAttributes)
				.anyMatch(CollectionUtils::isNotEmpty);
	}
}
