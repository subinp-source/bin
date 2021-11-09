/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorfacades.query;

import java.util.HashMap;
import java.util.Map;


/**
 * Simple Builder utility class which makes the parameters construction easier
 */
public final class QueryParameters
{

	final private Map<String, String> parameters;

	private QueryParameters(final String name, final String value)
	{
		this.parameters = new HashMap<>();
		this.parameters.put(name, value);
	}

	public static QueryParameters with(final String name, final String value)
	{
		return new QueryParameters(name, value);
	}

	public QueryParameters and(final String name, final String value)
	{
		this.parameters.put(name, value);
		return this;
	}

	public Map buildMap()
	{
		return this.parameters;
	}
}
