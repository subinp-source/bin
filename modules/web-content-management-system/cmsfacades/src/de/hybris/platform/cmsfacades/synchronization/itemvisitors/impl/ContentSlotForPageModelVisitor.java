/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.synchronization.itemvisitors.impl;

import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.visitor.ItemVisitor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * Concrete implementation of {@link ItemVisitor} to visit items of the {@link ContentSlotForPageModel} types.
 * Collects the item from {@link ContentSlotForPageModel#getContentSlot()}
 */
public class ContentSlotForPageModelVisitor implements ItemVisitor<ContentSlotForPageModel>
{
	@Override public List<ItemModel> visit(ContentSlotForPageModel contentSlotForPageModel, List<ItemModel> list,
			Map<String, Object> map)
	{
		return Arrays.asList(contentSlotForPageModel.getContentSlot());
	}
}
