/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence.populator;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

import java.util.Map;

/**
 * This abstract map attribute populator contains common logic for handling map attributes
 */
public abstract class AbstractMapAttributePopulator extends AbstractAttributePopulator
{
	/**
	 * Provides the implementation on how to set the map attribute
	 * @param item Item to set
	 * @param attribute Metadata on the attribute being set
	 * @param newValue value to set on the attribute
	 */
	protected abstract void setNewMapValues(final ItemModel item, final TypeAttributeDescriptor attribute, final Map<?, ?> newValue);

	@Override
	protected void populateAttribute(final ItemModel item, final TypeAttributeDescriptor attribute,
	                                 final PersistenceContext context)
	{
		final var newValue = context.getIntegrationItem().getAttribute(attribute);
		if (newValue instanceof Map)
		{
			nonNullableMapKeyValidation(attribute, context, (Map<?, ?>) newValue);
			setNewMapValues(item, attribute, (Map<?, ?>) newValue);
		}
	}

	private void nonNullableMapKeyValidation(final TypeAttributeDescriptor attribute, final PersistenceContext context,
	                                         final Map<?, ?> newValues)
	{
		if (newValues.containsKey(null))
		{
			throw new MissingRequiredMapKeyValueException(attribute, context);
		}
	}
}
