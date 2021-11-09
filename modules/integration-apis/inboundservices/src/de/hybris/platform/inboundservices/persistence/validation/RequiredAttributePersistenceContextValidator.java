/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.inboundservices.persistence.validation;

import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.inboundservices.persistence.populator.MissingRequiredAttributeValueException;
import de.hybris.platform.integrationservices.item.IntegrationItem;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

/**
 * Validates that non-nullable attributes have non-null values before persistence.
 */
public class RequiredAttributePersistenceContextValidator implements PersistenceContextValidator
{
	@Override
	public void validate(final PersistenceContext context)
	{
		final IntegrationItem item = context.getIntegrationItem();
		item.getItemType().getAttributes().stream()
		    .filter(descriptor -> !descriptor.isNullable())
		    .filter(descriptor -> item.getAttribute(descriptor) == null)
		    .findFirst()
		    .ifPresent(descriptor -> rejectAttribute(descriptor, context));

	}

	private void rejectAttribute(final TypeAttributeDescriptor descriptor, final PersistenceContext context)
	{
		throw new MissingRequiredAttributeValueException(descriptor, context);
	}
}
