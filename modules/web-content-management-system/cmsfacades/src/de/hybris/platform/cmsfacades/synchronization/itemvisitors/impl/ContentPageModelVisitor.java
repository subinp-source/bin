/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.synchronization.itemvisitors.impl;

import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cmsfacades.synchronization.itemvisitors.AbstractPageModelVisitor;
import de.hybris.platform.core.model.ItemModel;

import java.util.List;
import java.util.Map;


/**
 * Concrete implementation of {@link AbstractPageModelVisitor} to visit items of the {@link ContentPageModel} types.
 *
 * Collects the items from {@link AbstractPageModelVisitor#visit(AbstractPageModel, List, Map)}.
 *
 */
public class ContentPageModelVisitor extends AbstractPageModelVisitor<ContentPageModel>
{

	@Override
	public List<ItemModel> visit(final ContentPageModel source, final List<ItemModel> path, final Map<String, Object> ctx)
	{
		return super.visit(source, path, ctx);
	}

}
