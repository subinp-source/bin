/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.synchronization.itemvisitors.impl;

import static com.google.common.collect.Lists.newLinkedList;

import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.visitor.ItemVisitor;

import java.util.List;
import java.util.Map;

/**
 * Concrete implementation of {@link ItemVisitor} to visit items of the {@link ContentSlotModel} types.
 *
 * Collects the items from {@link ContentSlotModel#getCmsComponents()} 
 */
public class ContentSlotModelVisitor implements ItemVisitor<ContentSlotModel>
{

	@Override
	public List<ItemModel> visit(ContentSlotModel source, List<ItemModel> path, Map<String, Object> ctx)
	{
		return newLinkedList(source.getCmsComponents());
	}

}
