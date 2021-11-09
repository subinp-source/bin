/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.search.validation;

import de.hybris.platform.integrationservices.item.IntegrationItem;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.search.ItemSearchRequest;

/**
 * A validator that ensures presence of all key properties values in cases when search is done by the item key, i.e. unique item search.
 */
public class KeyPropertyValuePresenceValidator implements ItemSearchRequestValidator
{
	@Override
	public void validate(final ItemSearchRequest request)
	{
		request.getRequestedItem().ifPresent(item -> validate(request, item));
	}

	private void validate(final ItemSearchRequest request,
	                                   final IntegrationItem item)
	{
		item.getItemType().getAttributes().stream()
		    .filter(TypeAttributeDescriptor::isKeyAttribute)
		    .filter(attr -> !attr.isNullable())
		    .forEach(attr -> validate(request, item, attr));
	}

	private void validate(final ItemSearchRequest request,
	                      final IntegrationItem item,
	                      final TypeAttributeDescriptor attr)
	{
		if (item.getAttribute(attr) == null && !attr.isNullable())
		{
			throw new MissingRequiredKeyAttributeValueException(request, attr);
		}
		if (!attr.isPrimitive())
		{
			validate(request, item.getReferencedItem(attr));
		}
	}
}
