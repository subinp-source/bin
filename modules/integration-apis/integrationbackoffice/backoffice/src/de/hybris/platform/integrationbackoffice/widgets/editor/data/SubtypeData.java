/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.editor.data;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;

public class SubtypeData
{

	private ComposedTypeModel parentNodeType;
	private TypeModel subtype;
	private TypeModel baseType;
	private String attributeAlias;
	private String attributeQualifier;

	public SubtypeData(final ComposedTypeModel parentType, final TypeModel subtype, final TypeModel baseType,
	                   final String attributeAlias, final String attributeQualifier)
	{
		this.parentNodeType = parentType;
		this.subtype = subtype;
		this.baseType = baseType;
		this.attributeAlias = attributeAlias;
		this.attributeQualifier = attributeQualifier;
	}

	public ComposedTypeModel getParentNodeType()
	{
		return parentNodeType;
	}

	public void setSubtype(final TypeModel subtype)
	{
		this.subtype = subtype;
	}

	public TypeModel getSubtype()
	{
		return subtype;
	}

	public TypeModel getBaseType()
	{
		return baseType;
	}

	public String getAttributeAlias()
	{
		return attributeAlias;
	}

	public String getAttributeQualifier()
	{
		return attributeQualifier;
	}

}
