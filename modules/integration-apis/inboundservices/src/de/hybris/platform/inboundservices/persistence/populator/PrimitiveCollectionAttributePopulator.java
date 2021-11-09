/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence.populator;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.inboundservices.persistence.PrimitiveValueHandler;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Populates a collection of primitives
 */
public class PrimitiveCollectionAttributePopulator extends AbstractCollectionAttributePopulator
{
    private PrimitiveValueHandler primitiveValueHandler = new NullPrimitiveValueHandler();
	@Override
	protected boolean isApplicable(final TypeAttributeDescriptor attribute, final PersistenceContext context)
	{
		return !context.isReplaceAttributes() && !attribute.isLocalized() && attribute.isPrimitive() && attribute.isCollection();
	}

	@Override
	protected Collection<Object> getNewCollection(final ItemModel item, final TypeAttributeDescriptor attributeDescriptor, final PersistenceContext context)
	{
		final Object obj = context.getIntegrationItem().getAttribute(attributeDescriptor);
		//noinspection unchecked
		return obj instanceof Collection
				? ((Collection<Object>) obj).stream()
                                            .filter(Objects::nonNull)
                                            .map(value -> primitiveValueHandler.convert(attributeDescriptor, value))
                                            .collect(Collectors.toList())
				: Collections.emptyList();
	}

    public void setPrimitiveValueHandler(final PrimitiveValueHandler primitiveValueHandler)
    {
        this.primitiveValueHandler = primitiveValueHandler;
    }
}
