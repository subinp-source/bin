/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.spring.config;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * Class for testing list merge directive and map merge directive
 */
public class MergeTestBean
{
	private List<String> stringList;
	private List<Object> objectList;
	private List<BigDecimal> decimalList;
	private List<String> nullStringList;


	private Map<String, String> stringMap;
	private Map<String, Collection<String>> multiMap;
	private Map<String, String> nullStringMap;

	public MergeTestBean()
	{

	}

	public List<String> getStringList()
	{
		return stringList;
	}

	public void setStringList(final List<String> stringList)
	{
		this.stringList = stringList;
	}

	public List<Object> getObjectList()
	{
		return objectList;
	}

	public void setObjectList(final List<Object> objectList)
	{
		this.objectList = objectList;
	}

	public void setDecimalList(final List<BigDecimal> decimalList)
	{
		this.decimalList = decimalList;
	}

	public List<BigDecimal> getDecimalList()
	{
		return decimalList;
	}

	public Map<String, String> getStringMap()
	{
		return stringMap;
	}

	public void setStringMap(final Map<String, String> stringMap)
	{
		this.stringMap = stringMap;
	}

	public Map<String, Collection<String>> getMultiMap()
	{
		return multiMap;
	}

	public void setMultiMap(final Map<String, Collection<String>> multiMap)
	{
		this.multiMap = multiMap;
	}

	public List<String> getNullStringList()
	{
		return nullStringList;
	}

	public void setNullStringList(final List<String> nullStringList)
	{
		this.nullStringList = nullStringList;
	}

	public Map<String, String> getNullStringMap()
	{
		return nullStringMap;
	}

	public void setNullStringMap(final Map<String, String> nullStringMap)
	{
		this.nullStringMap = nullStringMap;
	}
}
