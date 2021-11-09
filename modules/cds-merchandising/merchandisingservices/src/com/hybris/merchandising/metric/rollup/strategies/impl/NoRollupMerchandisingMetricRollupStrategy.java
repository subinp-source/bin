/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.metric.rollup.strategies.impl;

import java.util.Optional;

import com.hybris.merchandising.exceptions.MerchandisingMetricRollupException;
import com.hybris.merchandising.metric.rollup.strategies.MerchandisingMetricRollupStrategy;

import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;

/**
 * Essentially a no-op implementation an {@link MerchandisingMetricRollupStrategy}. No rollup is performed, so scores
 * will be calculated for every product variant individually.
 */
public class NoRollupMerchandisingMetricRollupStrategy implements MerchandisingMetricRollupStrategy
{
	private static final String ROLLUP_STRATEGY_NAME = "No Rollup";
	private static final int SORT_ORDER = 100;

	@Override
	public String getReportingGroup(final InputDocument product,final String fieldName) throws MerchandisingMetricRollupException
	{
		return Optional.ofNullable(product.getFieldValue(fieldName))
			.map(String::valueOf)
			.orElseThrow(() -> new MerchandisingMetricRollupException(
				String.format(
					"Cannot calculate reporting group for product as no field could be found with name [%s].",
					fieldName)));
	}

	@Override
	public String getName()
	{
		return ROLLUP_STRATEGY_NAME;
	}

	@Override
	public int getOrder()
	{
		return SORT_ORDER;
	}
}
