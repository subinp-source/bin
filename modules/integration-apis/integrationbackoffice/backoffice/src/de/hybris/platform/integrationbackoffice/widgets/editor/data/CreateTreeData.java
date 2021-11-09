/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.editor.data;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.integrationbackoffice.dto.AbstractListItemDTO;

import java.util.List;
import java.util.Map;

public class CreateTreeData
{
	private ComposedTypeModel root;
	private Map<ComposedTypeModel, List<AbstractListItemDTO>> definitionMap;

	public CreateTreeData(final ComposedTypeModel root, final Map<ComposedTypeModel, List<AbstractListItemDTO>> definitionMap)
	{
		this.root = root;
		this.definitionMap = definitionMap;
	}

	public ComposedTypeModel getRoot()
	{
		return root;
	}

	public Map<ComposedTypeModel, List<AbstractListItemDTO>> getDefinitionMap()
	{
		return definitionMap;
	}
}
