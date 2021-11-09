/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.core.service.impl;


import de.hybris.platform.searchservices.core.service.SnResponse;
import de.hybris.platform.searchservices.admin.data.SnIndexConfiguration;
import de.hybris.platform.searchservices.admin.data.SnIndexType;


/**
 * Base class for implementations of {@link SnResponse}.
 */
public abstract class AbstractSnResponse implements SnResponse
{
	private final SnIndexConfiguration indexConfiguration;
	private final SnIndexType indexType;

	public AbstractSnResponse(final SnIndexConfiguration indexConfiguration, final SnIndexType indexType)
	{
		this.indexConfiguration = indexConfiguration;
		this.indexType = indexType;
	}

	@Override
	public SnIndexConfiguration getIndexConfiguration()
	{
		return indexConfiguration;
	}

	@Override
	public SnIndexType getIndexType()
	{
		return indexType;
	}
}
