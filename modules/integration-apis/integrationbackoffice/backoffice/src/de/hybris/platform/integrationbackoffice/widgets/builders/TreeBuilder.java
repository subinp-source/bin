/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.builders;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.integrationbackoffice.widgets.editor.data.TreeNodeData;

import java.util.Deque;

import org.zkoss.zul.Treeitem;

/**
 * Interface for extracted tree methods from EditorutilsController
 */
public interface TreeBuilder
{
	public Deque<ComposedTypeModel> determineTreeitemAncestors(Treeitem currentTreeitem);

	public Treeitem appendTreeitem(final Treeitem parent, final TreeNodeData tnd);
}
