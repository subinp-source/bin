/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence.populator;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.inboundservices.persistence.AttributePopulator;
import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Required;

/**
 * An implementation of an {@link AttributePopulator}, that handles collections of items models by replacing all
 * elements pre-existing in the persistent storage with the values present in the payload only.
 */
public class ReplaceItemModelCollectionAttributePopulator extends AbstractAttributePopulator
{
	private ContextReferencedItemModelService contextReferencedItemModelService;

	@Override
	protected boolean isApplicable(final TypeAttributeDescriptor attribute, final PersistenceContext context)
	{
		return context.isReplaceAttributes()
				&& attribute.isCollection()
				&& !attribute.isPrimitive();
	}

	@Override
	protected void populateAttribute(final ItemModel item, final TypeAttributeDescriptor attribute,
	                                 final PersistenceContext context)
	{
		final Collection<?> newValues = derivePayloadItems(attribute, context);
		attribute.accessor().setValue(item, newValues);
	}

	private Collection<?> derivePayloadItems(final TypeAttributeDescriptor attribute, final PersistenceContext context)
	{
		final var resolvedItems = contextReferencedItemModelService.deriveItemsReferencedInAttributeValue(context,
				attribute);
		final var newItems = attribute.getCollectionDescriptor().newCollection();
		newItems.addAll(resolvedItems);
		return context.getIntegrationItem().getAttribute(attribute) == null
				? null
				: newItems;
	}

	@Required
	public void setContextReferencedItemModelService(
			final ContextReferencedItemModelService contextReferencedItemModelService)
	{
		this.contextReferencedItemModelService = contextReferencedItemModelService;
	}
}
