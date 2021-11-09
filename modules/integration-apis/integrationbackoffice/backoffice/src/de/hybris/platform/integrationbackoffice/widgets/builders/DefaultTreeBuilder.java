/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.builders;

import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorUtils.createTreeItem;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.integrationbackoffice.widgets.editor.data.TreeNodeData;

import java.util.ArrayDeque;
import java.util.Deque;

import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;

/**
 * Default implementation of tree builder
 */
public class DefaultTreeBuilder implements TreeBuilder
{
	@Override
	public Deque<ComposedTypeModel> determineTreeitemAncestors(Treeitem currentTreeitem)
	{
		final Deque<ComposedTypeModel> currentTreePath = new ArrayDeque<>();
		while (currentTreeitem.getLevel() > 0)
		{
			currentTreePath.addFirst(((TreeNodeData) currentTreeitem.getValue()).getComposedTypeModel());
			currentTreeitem = currentTreeitem.getParentItem();
		}
		currentTreePath.addFirst(((TreeNodeData) currentTreeitem.getValue()).getComposedTypeModel());
		return currentTreePath;
	}

	@Override
	public Treeitem appendTreeitem(final Treeitem parent, final TreeNodeData tnd)
	{
		final Treeitem treeitem = createTreeItem(tnd, false);
		if (parent.getTreechildren() == null)
		{
			parent.appendChild(new Treechildren());
		}
		parent.getTreechildren().appendChild(treeitem);
		return treeitem;
	}
}
