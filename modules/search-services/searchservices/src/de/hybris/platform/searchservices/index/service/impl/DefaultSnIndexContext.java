/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.index.service.impl;


import de.hybris.platform.searchservices.core.service.impl.DefaultSnContext;
import de.hybris.platform.searchservices.index.service.SnIndexContext;


/**
 * Default implementation of {@link SnIndexContext}.
 */
public class DefaultSnIndexContext extends DefaultSnContext implements SnIndexContext
{
	private String indexId;

	@Override
	public String getIndexId()
	{
		return indexId;
	}

	@Override
	public void setIndexId(final String indexId)
	{
		this.indexId = indexId;
	}
}
