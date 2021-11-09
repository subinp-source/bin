/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.spring.config;

import java.util.HashMap;
import java.util.Map;


public class MultipleMapMergeBean
{
	private Map<String, String> propertyMap;
	private Map<String, String> fieldMap;

	public MultipleMapMergeBean()
	{
		propertyMap = new HashMap<String, String>();
		fieldMap = new HashMap<String, String>();
	}

	public Map<String, String> getPropertyMap()
	{
		return propertyMap;
	}

	public void setPropertyMap(final Map<String, String> propertyMap)
	{
		this.propertyMap = propertyMap;
	}

	public void setFieldMap(final Map<String, String> fieldMap)
	{
		this.fieldMap = fieldMap;
	}
}