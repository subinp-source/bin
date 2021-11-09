/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.model.AttributeSettableChecker;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

/**
 * An attribute settable checker that always returns {@code false}.
 */
public class NullAttributeSettableChecker implements AttributeSettableChecker
{
	@Override
	public boolean isSettable(final ItemModel item, final TypeAttributeDescriptor attribute)
	{
		return false;
	}
}
