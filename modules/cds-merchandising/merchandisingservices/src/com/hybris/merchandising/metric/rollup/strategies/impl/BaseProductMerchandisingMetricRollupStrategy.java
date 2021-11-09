/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.metric.rollup.strategies.impl;

import java.util.Optional;

import com.hybris.merchandising.exceptions.MerchandisingMetricRollupException;
import com.hybris.merchandising.metric.rollup.strategies.MerchandisingMetricRollupStrategy;

import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;

/**
 * An implementation of a {@link MerchandisingMetricRollupStrategy} that rolls up to the base product level.
 * For each base product one of its variant's will be chosen to attribute all other variants' scores to.
 */
public class BaseProductMerchandisingMetricRollupStrategy implements MerchandisingMetricRollupStrategy
{

	private static final String ROLLUP_STRATEGY_NAME = "Rollup to Base Product";
	private static final int SORT_ORDER = 200;

	@Override
	public String getReportingGroup(final InputDocument product, final String fieldName) throws MerchandisingMetricRollupException
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