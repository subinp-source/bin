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
public class NoRollupMerchandisingMetricRollupStrategyTest
{
	private static final String PRODUCT_CODE_FIELD_NAME = "code_string";
	private static final String PRODUCT_CODE = "test-product-code";

	@Mock
	private InputDocument inputDocument;

	private static final NoRollupMerchandisingMetricRollupStrategy NO_ROLLUP_MERCHANDISING_METRIC_ROLLUP_STRATEGY
		= new NoRollupMerchandisingMetricRollupStrategy();

	@Test
	public void testGetReportingGroupNoCodeFieldOnProduct()
	{
		when(inputDocument.getFieldValue(PRODUCT_CODE_FIELD_NAME)).thenReturn(null);

		assertThatExceptionOfType(MerchandisingMetricRollupException.class)
			.isThrownBy(() -> NO_ROLLUP_MERCHANDISING_METRIC_ROLLUP_STRATEGY.getReportingGroup(inputDocument,null));
	}

	@Test
	public void testGetReportingGroupNoCodeField() throws MerchandisingMetricRollupException
	{
		when(inputDocument.getFieldValue(PRODUCT_CODE_FIELD_NAME)).thenReturn(PRODUCT_CODE);

		assertThat(NO_ROLLUP_MERCHANDISING_METRIC_ROLLUP_STRATEGY.getReportingGroup(inputDocument,PRODUCT_CODE_FIELD_NAME))
			.isEqualTo(PRODUCT_CODE);
	}
}