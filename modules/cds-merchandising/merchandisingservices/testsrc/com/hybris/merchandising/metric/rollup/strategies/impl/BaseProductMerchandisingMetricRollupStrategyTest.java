/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.metric.rollup.strategies.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.merchandising.exceptions.MerchandisingMetricRollupException;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class BaseProductMerchandisingMetricRollupStrategyTest
{
	private static final String BASE_PRODUCT_CODE_FIELD_NAME = "baseProductCode_string";
	private static final String BASE_PRODUCT_CODE = "test-base-product-code";

	@Mock
	private InputDocument inputDocument;

	private static final BaseProductMerchandisingMetricRollupStrategy
		MASTER_PRODUCT_MERCHANDISING_METRIC_ROLLUP_STRATEGY = new BaseProductMerchandisingMetricRollupStrategy();

	@Test
	public void testGetReportingGroupNoCodeFieldOnProduct()
	{
		when(inputDocument.getFieldValue(BASE_PRODUCT_CODE_FIELD_NAME)).thenReturn(null);

		assertThatExceptionOfType(MerchandisingMetricRollupException.class)
			.isThrownBy(() -> MASTER_PRODUCT_MERCHANDISING_METRIC_ROLLUP_STRATEGY.getReportingGroup(inputDocument,null));
	}

	@Test
	public void testGetReportingGroupNoCodeField() throws MerchandisingMetricRollupException
	{
		when(inputDocument.getFieldValue(BASE_PRODUCT_CODE_FIELD_NAME)).thenReturn(BASE_PRODUCT_CODE);

		assertThat(MASTER_PRODUCT_MERCHANDISING_METRIC_ROLLUP_STRATEGY.getReportingGroup(inputDocument,BASE_PRODUCT_CODE_FIELD_NAME))
			.isEqualTo(BASE_PRODUCT_CODE);
	}
}