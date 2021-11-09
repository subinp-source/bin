/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.editor.data;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.integrationbackoffice.dto.AbstractListItemDTO;

public class RenameTreeData
{
	private ComposedTypeModel parentComposedType;
	private AbstractListItemDTO matchedDTO;
	private String qualifier;

	public RenameTreeData(final ComposedTypeModel parentComposedType,
	                      final AbstractListItemDTO matchedDTO, final String qualifier)
	{
		this.parentComposedType = parentComposedType;
		this.matchedDTO = matchedDTO;
		this.qualifier = qualifier;
	}

	public ComposedTypeModel getParentComposedType()
	{
		return parentComposedType;
	}

	public AbstractListItemDTO getMatchedDTO()
	{
		return matchedDTO;
	}

	public String getQualifier()
	{
		return qualifier;
	}
}
