/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence.populator;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.inboundservices.persistence.AttributePopulator;
import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

import org.springframework.beans.factory.annotation.Required;

/**
 * An implementation of an {@link AttributePopulator}, that handles populating a {@link ItemModel}s
 * attribute for a single items model reference {@link TypeAttributeDescriptor}.
 */
public class ItemModelAttributePopulator extends AbstractAttributePopulator
{
	private ContextReferencedItemModelService contextReferencedItemModelService;

	@Override
	protected boolean isApplicable(final TypeAttributeDescriptor attribute, final PersistenceContext context)
	{
		return !attribute.isCollection() && !attribute.isPrimitive() && !attribute.isMap();
	}

	@Override
	protected void populateAttribute(final ItemModel item, final TypeAttributeDescriptor attribute,
	                                 final PersistenceContext context)
	{
		final ItemModel referencedItem = contextReferencedItemModelService.deriveReferencedItemModel(attribute,
				context.getReferencedContext(attribute));
		final Object referencedItemValue = attribute.getAttributeType().isEnumeration()
				? getModelService().get(referencedItem.getPk())
				: referencedItem;
		attribute.accessor().setValue(item, referencedItemValue);
	}

	@Required
	public void setContextReferencedItemModelService(
			final ContextReferencedItemModelService contextReferencedItemModelService)
	{
		this.contextReferencedItemModelService = contextReferencedItemModelService;
	}
}
