/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.editor.data;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.integrationbackoffice.dto.ListItemAttributeDTO;

import org.zkoss.zul.Treeitem;

public class RetypeTreeData
{

	final ComposedTypeModel parentComposedType;
	final ComposedTypeModel newComposedType;
	final Treeitem currentTreeitem;
	final ListItemAttributeDTO dto;

	public RetypeTreeData(final ComposedTypeModel parentComposedType, final ComposedTypeModel newComposedType,
	                      final Treeitem currentTreeitem, final ListItemAttributeDTO dto)
	{
		this.parentComposedType = parentComposedType;
		this.newComposedType = newComposedType;
		this.currentTreeitem = currentTreeitem;
		this.dto = dto;
	}

	public ComposedTypeModel getParentComposedType()
	{
		return parentComposedType;
	}

	public ComposedTypeModel getNewComposedType()
	{
		return newComposedType;
	}

	public Treeitem getCurrentTreeitem()
	{
		return currentTreeitem;
	}

	public ListItemAttributeDTO getDto()
	{
		return dto;
	}
}
