/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence.populator;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.inboundservices.persistence.AttributePopulator;
import de.hybris.platform.inboundservices.persistence.ItemModelPopulator;
import de.hybris.platform.inboundservices.persistence.PersistenceContext;

import java.util.Collections;
import java.util.List;

import com.google.common.base.Preconditions;

/**
 * An implementation that uses {@link AttributePopulator}s to populate the item attributes.
 */
public class DefaultItemModelPopulator implements ItemModelPopulator
{
	private List<AttributePopulator> attributePopulators = Collections.emptyList();

	@Override
	public void populate(final ItemModel item, final PersistenceContext context)
	{
		Preconditions.checkArgument(item != null, "ItemModel cannot be null");
		Preconditions.checkArgument(context != null, "PersistenceContext cannot be null");
		attributePopulators.forEach(p -> p.populate(item, context));
	}

	public void setAttributePopulators(final List<AttributePopulator> populators)
	{
		attributePopulators = populators != null ? populators : Collections.emptyList();
	}
}
