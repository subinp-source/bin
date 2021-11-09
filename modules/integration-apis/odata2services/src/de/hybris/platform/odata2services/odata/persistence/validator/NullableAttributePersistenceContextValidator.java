/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.persistence.validator;

import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.inboundservices.persistence.validation.RequiredAttributePersistenceContextValidator;
import de.hybris.platform.integrationservices.item.IntegrationItem;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.odata2services.odata.persistence.exception.MissingNavigationPropertyException;
import de.hybris.platform.odata2services.odata.persistence.exception.MissingPropertyException;

/**
 * @deprecated replaced by {@link RequiredAttributePersistenceContextValidator}
 */
@Deprecated(since = "2105", forRemoval = true)
public class NullableAttributePersistenceContextValidator extends RequiredAttributePersistenceContextValidator
{
	@Override
	public void validate(final PersistenceContext context)
	{
		final IntegrationItem item = context.getIntegrationItem();

		item.getItemType().getAttributes().stream()
		    .filter(descriptor -> !descriptor.isNullable())
		    .filter(descriptor -> item.getAttribute(descriptor) == null)
		    .anyMatch(descriptor -> validateAttribute(item, descriptor));
	}

	private boolean validateAttribute(final IntegrationItem item, final TypeAttributeDescriptor descriptor)
	{
		if (isReference(descriptor))
		{
			throw new MissingNavigationPropertyException(item.getItemType().getItemCode(), descriptor.getAttributeName());
		}
		throw new MissingPropertyException(item.getItemType().getItemCode(), descriptor.getAttributeName());
	}

	private boolean isReference(final TypeAttributeDescriptor descriptor)
	{
		return descriptor.isCollection() || descriptor.isMap() || !descriptor.isPrimitive();
	}
}
