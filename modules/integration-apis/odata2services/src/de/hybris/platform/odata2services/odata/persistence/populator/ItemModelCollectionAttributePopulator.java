/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.persistence.populator;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.inboundservices.persistence.populator.AbstractCollectionAttributePopulator;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Required;

/**
 * @deprecated use {@link de.hybris.platform.inboundservices.persistence.populator.ItemModelCollectionAttributePopulator}
 */
@Deprecated(since = "21.05.0-RC1", forRemoval = true)
public class ItemModelCollectionAttributePopulator extends AbstractCollectionAttributePopulator
{
	private de.hybris.platform.inboundservices.persistence.populator.ContextReferencedItemModelService contextReferencedItemModelService;

	@Override
	protected boolean isApplicable(final TypeAttributeDescriptor attribute, final PersistenceContext context)
	{
		return !context.isReplaceAttributes() && !attribute.isPrimitive() && attribute.isCollection();
	}

	@Override
	protected Collection<Object> getNewCollection(final ItemModel item, final TypeAttributeDescriptor collectionAttribute, final PersistenceContext context)
	{
		final Collection<ItemModel> models = getContextReferencedItemModelService().deriveItemsReferencedInAttributeValue(context, collectionAttribute);
		return new ArrayList<>(models);
	}

	protected de.hybris.platform.inboundservices.persistence.populator.ContextReferencedItemModelService getContextReferencedItemModelService()
	{
		return contextReferencedItemModelService;
	}

	@Required
	public void setContextReferencedItemModelService(final de.hybris.platform.inboundservices.persistence.populator.ContextReferencedItemModelService service)
	{
		contextReferencedItemModelService = service;
	}
}
