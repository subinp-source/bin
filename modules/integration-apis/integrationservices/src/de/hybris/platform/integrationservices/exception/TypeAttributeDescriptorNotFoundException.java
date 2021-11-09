/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.exception;

import de.hybris.platform.integrationservices.model.TypeDescriptor;

/**
 * Use this exception when an Integration Object doesn't contain the attribute descriptor for the specified attribute
 */
public class TypeAttributeDescriptorNotFoundException extends RuntimeException
{
	private final transient TypeDescriptor attributeDescriptor;
	private final String attrName;

	public TypeAttributeDescriptorNotFoundException(final TypeDescriptor attributeDescriptor, final String attrName)
	{
		super(String.format("Attribute [%s] is not defined for Item [%s] in IntegrationObject [%s].",
				attrName, attributeDescriptor.getItemCode(), attributeDescriptor.getIntegrationObjectCode()));
		this.attributeDescriptor = attributeDescriptor;
		this.attrName = attrName;
	}

	public TypeDescriptor getAttributeDescriptor()
	{
		return attributeDescriptor;
	}

	public String getAttrName()
	{
		return attrName;
	}
}
