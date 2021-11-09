/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.inboundservices.persistence.CannotCreateReferencedItemException;
import de.hybris.platform.inboundservices.persistence.ContextItemModelService;
import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.inboundservices.persistence.populator.ContextReferencedItemModelService;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Required;

/**
 * A service for searching for & creating items from the nested representation of these items in the payload for persistence.
 */
public class DefaultContextReferencedItemModelService implements ContextReferencedItemModelService
{
	private ContextItemModelService contextItemModelService;

	@Override
	public Collection<ItemModel> deriveItemsReferencedInAttributeValue(final PersistenceContext context,
	                                                                   final TypeAttributeDescriptor attribute)
	{
		return context.getReferencedContexts(attribute).stream()
		              .map(refItemCtx -> deriveReferencedItemModel(attribute, refItemCtx))
		              .collect(Collectors.toList());
	}

	@Override
	public ItemModel deriveReferencedItemModel(final TypeAttributeDescriptor attribute,
	                                           final PersistenceContext referencedItemContext)
	{
		final ItemModel item = contextItemModelService.findOrCreateItem(referencedItemContext);
		return Optional.ofNullable(item)
		               .orElseThrow(() -> new CannotCreateReferencedItemException(attribute, referencedItemContext));
	}

	@Required
	public void setContextItemModelService(final ContextItemModelService contextItemModelService)
	{
		this.contextItemModelService = contextItemModelService;
	}
}
