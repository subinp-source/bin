/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.metric.rollup.strategies;

import com.hybris.merchandising.exceptions.MerchandisingMetricRollupException;

import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;

/**
 * An interface that defines a strategy for "rolling up" variants for merchandising metric calculations.
 */
public interface MerchandisingMetricRollupStrategy
{
	/**
	 * Gets the "rolled up" group that the given product should have its merchandising scores calculated against.
	 *
	 * @param product - the product to calculate the "rolled up" reporting group for
	 * @param fieldName - "rolled up" variant field name.
	 * @return - the "rolled up" reporting group for the given product
	 * @throws MerchandisingMetricRollupException if the reporting group could not be calculated for the product
	 */
	String getReportingGroup(final InputDocument product, final String fieldName) throws MerchandisingMetricRollupException;

	/**
	 * Gets the display name of the rollup strategy.
	 *
	 * @return the display name of the rollup strategy
	 */
	String getName();

	/**
	 * Gets the relative order position of the given rollup strategy. This is used to determine the position (relative
	 * to other strategies) of the strategy in the rollup strategy dropdown.
	 *
	 * @return the relative order position relative to other rollup strategies
	 */
	int getOrder();
}
