/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence.populator;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.inboundservices.persistence.AttributePopulator;
import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Required;

/**
 * An implementation of an {@link AttributePopulator}, that handles collections of items models by adding any
 * new elements that have not been persisted in the {@link ItemModel}s collection for the {@link TypeAttributeDescriptor}
 * that is being populated.
 */
public class ItemModelCollectionAttributePopulator extends AbstractCollectionAttributePopulator
{
	private ContextReferencedItemModelService contextReferencedItemModelService;

	@Override
	protected boolean isApplicable(final TypeAttributeDescriptor attribute, final PersistenceContext context)
	{
		return !context.isReplaceAttributes() && !attribute.isPrimitive() && attribute.isCollection();
	}

	@Override
	protected Collection<Object> getNewCollection(final ItemModel item, final TypeAttributeDescriptor collectionAttribute,
	                                              final PersistenceContext context)
	{
		final Collection<ItemModel> models = contextReferencedItemModelService
				.deriveItemsReferencedInAttributeValue(context, collectionAttribute);
		return new ArrayList<>(models);
	}

	@Required
	public void setContextReferencedItemModelService(
			final ContextReferencedItemModelService contextReferencedItemModelService)
	{
		this.contextReferencedItemModelService = contextReferencedItemModelService;
	}
}
