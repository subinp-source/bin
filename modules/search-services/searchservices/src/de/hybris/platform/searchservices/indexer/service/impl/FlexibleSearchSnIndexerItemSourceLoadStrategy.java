/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service.impl;

import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSourceLoadStrategy;
import de.hybris.platform.searchservices.model.FlexibleSearchSnIndexerItemSourceModel;

import java.util.Map;


/**
 * Implementation of {@link SnIndexerItemSourceLoadStrategy} that uses flexible search queries.
 */
public class FlexibleSearchSnIndexerItemSourceLoadStrategy
		implements SnIndexerItemSourceLoadStrategy<FlexibleSearchSnIndexerItemSourceModel, FlexibleSearchSnIndexerItemSource>
{
	@Override
	public FlexibleSearchSnIndexerItemSource load(final FlexibleSearchSnIndexerItemSourceModel source,
			final Map<String, Object> parameters)
	{
		return new FlexibleSearchSnIndexerItemSource(source.getQuery(), parameters);
	}
}
