/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorfacades.component.synchronization.itemvisitors.impl;

import de.hybris.platform.acceleratorcms.model.components.NavigationComponentModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cmsfacades.synchronization.itemvisitors.AbstractCMSComponentModelVisitor;
import de.hybris.platform.core.model.ItemModel;

import java.util.List;
import java.util.Map;


/**
 * Concrete implementation of {@link AbstractCMSComponentModelVisitor} to visit items of the
 * {@link NavigationComponentModel} types.
 *
 * Collects the items from {@link AbstractCMSComponentModelVisitor#visit(AbstractCMSComponentModel, List, Map)} and
 * {@link NavigationComponentModel#getNavigationNode()}.
 */
public class NavigationComponentModelVisitor extends AbstractCMSComponentModelVisitor<NavigationComponentModel>
{

	@Override
	public List<ItemModel> visit(final NavigationComponentModel source, final List<ItemModel> path, final Map<String, Object> ctx)
	{
		final List<ItemModel> collectedItems = super.visit(source, path, ctx);

		collectedItems.add(source.getNavigationNode());

		return collectedItems;
	}

}
