/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.synchronization.itemcollector;

import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cmsfacades.common.itemcollector.ItemCollector;
import de.hybris.platform.core.model.ItemModel;

import java.util.List;

/**
 * Collects the direct cms components of a given {@link ContentSlotModel}.
 */
public class BasicContentSlotItemCollector implements ItemCollector<ContentSlotModel>
{
	@Override
	public List<? extends ItemModel> collect(final ContentSlotModel item)
	{
		return item.getCmsComponents();
	}
}
