/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.editor.data;

import de.hybris.platform.core.model.type.ComposedTypeModel;

public class TreeNodeData
{

	private String qualifier;
	private String alias;
	private ComposedTypeModel composedTypeModel;

	public TreeNodeData(final String qualifier, final String alias, final ComposedTypeModel composedTypeModel)
	{
		this.qualifier = qualifier;
		this.alias = (alias == null) ? qualifier : alias;
		this.composedTypeModel = composedTypeModel;
	}

	public String getQualifier()
	{
		return qualifier;
	}

	public ComposedTypeModel getComposedTypeModel()
	{
		return composedTypeModel;
	}

	public void setComposedTypeModel(ComposedTypeModel composedTypeModel)
	{
		this.composedTypeModel = composedTypeModel;
	}

	public String getAlias()
	{
		return alias;
	}

	public void setAlias(String alias)
	{
		this.alias = alias;
	}

}
