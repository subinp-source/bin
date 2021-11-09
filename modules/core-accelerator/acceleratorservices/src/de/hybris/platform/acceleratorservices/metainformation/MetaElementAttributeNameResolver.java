/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.metainformation;

import java.util.Map;


public class MetaElementAttributeNameResolver
{
	protected Map<String, String> mappedNames;

	public void setMappedNames(final Map<String, String> mappedNames)
	{
		this.mappedNames = mappedNames;
	}

	public String resolveName(final String fieldName)
	{
		if (mappedNames != null && mappedNames.containsKey(fieldName))
		{
			return mappedNames.get(fieldName);
		}
		return fieldName;
	}
}
