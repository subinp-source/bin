/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.core.service.impl;


import de.hybris.platform.searchservices.core.service.SnContext;
import de.hybris.platform.searchservices.core.service.SnQualifier;
import de.hybris.platform.searchservices.admin.data.SnIndexConfiguration;
import de.hybris.platform.searchservices.admin.data.SnIndexType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Default implementation of {@link SnContext}.
 */
public class DefaultSnContext implements SnContext
{
	private SnIndexConfiguration indexConfiguration;
	private SnIndexType indexType;
	private Map<String, List<SnQualifier>> qualifiers;

	private final Map<String, Object> attributes;
	private final List<Exception> exceptions;

	public DefaultSnContext()
	{
		attributes = new HashMap<>();
		exceptions = new ArrayList<>();
	}

	@Override
	public SnIndexConfiguration getIndexConfiguration()
	{
		return indexConfiguration;
	}

	public void setIndexConfiguration(final SnIndexConfiguration indexConfiguration)
	{
		this.indexConfiguration = indexConfiguration;
	}

	@Override
	public SnIndexType getIndexType()
	{
		return indexType;
	}

	public void setIndexType(final SnIndexType indexType)
	{
		this.indexType = indexType;
	}

	@Override
	public Map<String, List<SnQualifier>> getQualifiers()
	{
		return qualifiers;
	}

	public void setQualifiers(final Map<String, List<SnQualifier>> qualifiers)
	{
		this.qualifiers = qualifiers;
	}

	@Override
	public Map<String, Object> getAttributes()
	{
		return attributes;
	}

	@Override
	public void addException(final Exception exception)
	{
		exceptions.add(exception);
	}

	@Override
	public List<Exception> getExceptions()
	{
		return Collections.unmodifiableList(exceptions);
	}
}
