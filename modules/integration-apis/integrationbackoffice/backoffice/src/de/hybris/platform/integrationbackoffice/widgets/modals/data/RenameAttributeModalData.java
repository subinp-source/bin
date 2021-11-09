/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.modals.data;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.integrationbackoffice.dto.AbstractListItemDTO;

import java.util.List;

public class RenameAttributeModalData
{

	private List<AbstractListItemDTO> attributes;
	private AbstractListItemDTO dto;
	private ComposedTypeModel parent;

	public RenameAttributeModalData(final List<AbstractListItemDTO> attributes, final AbstractListItemDTO dto,
	                                final ComposedTypeModel parent)
	{
		this.attributes = attributes;
		this.dto = dto;
		this.parent = parent;
	}

	public List<AbstractListItemDTO> getAttributes()
	{
		return attributes;
	}

	public AbstractListItemDTO getDto()
	{
		return dto;
	}

	public ComposedTypeModel getParent()
	{
		return parent;
	}
}
