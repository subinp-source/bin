/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service.impl;

import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSourceLoadStrategy;
import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSourceMapping;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation for {@link SnIndexerItemSourceMapping}.
 */
public class DefaultSnIndexerItemSourceMapping implements SnIndexerItemSourceMapping
{
	private String itemType;
	private SnIndexerItemSourceLoadStrategy<?, ?> loadStrategy;

	@Override
	public String getItemType()
	{
		return itemType;
	}

	@Required
	public void setItemType(final String itemType)
	{
		this.itemType = itemType;
	}

	@Override
	public SnIndexerItemSourceLoadStrategy<?, ?> getLoadStrategy()
	{
		return loadStrategy;
	}

	@Required
	public void setLoadStrategy(final SnIndexerItemSourceLoadStrategy<?, ?> loadStrategy)
	{
		this.loadStrategy = loadStrategy;
	}
}
