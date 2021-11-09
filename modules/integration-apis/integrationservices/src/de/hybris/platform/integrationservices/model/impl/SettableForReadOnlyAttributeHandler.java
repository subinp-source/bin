/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.integrationservices.config.ReadOnlyAttributesConfiguration;
import de.hybris.platform.integrationservices.model.SettableAttributeHandler;

class SettableForReadOnlyAttributeHandler implements SettableAttributeHandler
{
	private final ReadOnlyAttributesConfiguration readOnlyAttributesConfiguration;

	public SettableForReadOnlyAttributeHandler(final ReadOnlyAttributesConfiguration readOnlyAttributesConfiguration)
	{
		this.readOnlyAttributesConfiguration = readOnlyAttributesConfiguration;
	}

	@Override
	public boolean isApplicable(final ItemModel item)
	{
		return true;
	}

	@Override
	public boolean isSettable(final AttributeDescriptorModel descriptor)
	{
		return !readOnlyAttributesConfiguration.getReadOnlyAttributes().contains(descriptor.getQualifier());
	}
}
