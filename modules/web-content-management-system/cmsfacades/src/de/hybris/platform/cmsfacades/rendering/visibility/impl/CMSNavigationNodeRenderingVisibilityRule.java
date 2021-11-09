/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.visibility.impl;

import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cmsfacades.rendering.visibility.RenderingVisibilityRule;
import de.hybris.platform.core.model.ItemModel;

import java.util.function.Predicate;


/**
 * Rendering visibility rule for {@link CMSNavigationNodeModel}
 */
public class CMSNavigationNodeRenderingVisibilityRule implements RenderingVisibilityRule<CMSNavigationNodeModel>
{
	@Override
	public Predicate<ItemModel> restrictedBy()
	{
		return itemModel -> CMSNavigationNodeModel.class.isAssignableFrom(itemModel.getClass());
	}

	@Override
	public boolean isVisible(CMSNavigationNodeModel itemModel)
	{
		return itemModel.isVisible();
	}
}
