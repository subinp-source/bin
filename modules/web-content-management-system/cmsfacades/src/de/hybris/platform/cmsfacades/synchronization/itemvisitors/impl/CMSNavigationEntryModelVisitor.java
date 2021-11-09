/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.synchronization.itemvisitors.impl;

import static com.google.common.collect.Lists.newArrayList;

import de.hybris.platform.cms2.model.navigation.CMSNavigationEntryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.visitor.ItemVisitor;

import java.util.List;
import java.util.Map;

/**
 * Concrete implementation of {@link ItemVisitor} to visit items of the {@link CMSNavigationEntryModel} type.
 *
 * Returns the items from {@link CMSNavigationEntryModel#getItem()} 
 *
 */
public class CMSNavigationEntryModelVisitor implements ItemVisitor<CMSNavigationEntryModel>
{

	@Override
	public List<ItemModel> visit(CMSNavigationEntryModel source, List<ItemModel> path, Map<String, Object> ctx)
	{
		return newArrayList(source.getItem());
	}

}
