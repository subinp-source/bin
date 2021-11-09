/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.merchandisingservicesbackoffice.editor.facade;


import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.merchandising.exceptions.MerchandisingMetricRollupException;
import com.hybris.merchandising.metric.rollup.strategies.MerchandisingMetricRollupStrategy;

import de.hybris.bootstrap.annotations.UnitTest;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class MerchandisingRollupStrategySearchFacadeTest
{
	private static final String NO_BEANS_OF_TYPE_EXCEPTION_MESSAGE
		= "No beans of type MerchandisingMetricRollupStrategy";
	private static final String DUPLICATE_BEANS_WITH_NAME_EXCEPTION_MESSAGE
		= "Multiple MerchandisingMetricRollupStrategy beans";

	private static final String NO_ROLLUP_STRATEGY_NAME = "No Rollup";
	private static final int NO_ROLLUP_SORT_ORDER = 1;
	private static final String BASE_PRODUCT_ROLLUP_STRATEGY_NAME = "Rollup to Base Product";
	private static final int BASE_PRODUCT_ROLLUP_SORT_ORDER = 2;
	private static final String CUSTOM_ROLLUP_STRATEGY = "Custom Rollup";
	private static final int CUSTOM_ROLLUP_SORT_ORDER = 3;

	@Mock
	private MerchandisingMetricRollupStrategy noRollupStrategy;
	@Mock
	private MerchandisingMetricRollupStrategy baseProductRollupStrategy;
	@Mock
	private MerchandisingMetricRollupStrategy customRollupStrategy;

	private MerchandisingRollupStrategySearchFacade merchandisingRollupStrategySearchFacade;

	@Before
	public void setup()
	{
		merchandisingRollupStrategySearchFacade = new MerchandisingRollupStrategySearchFacade();
	}

	@Test
	public void testSetRollupStrategiesNullStrategies()
	{
		assertThatExceptionOfType(MerchandisingMetricRollupException.class)
			.isThrownBy(() -> merchandisingRollupStrategySearchFacade.setRollupStrategies(null))
			.withMessageContaining(NO_BEANS_OF_TYPE_EXCEPTION_MESSAGE);
	}

	@Test
	public void testSetRollupStrategiesNoStrategies()
	{
		assertThatExceptionOfType(MerchandisingMetricRollupException.class)
			.isThrownBy(() -> merchandisingRollupStrategySearchFacade.setRollupStrategies(Collections.emptyList()))
			.withMessageContaining(NO_BEANS_OF_TYPE_EXCEPTION_MESSAGE);
	}

	@Test
	public void testSetRollupStrategiesDuplicateStrategyNames()
	{
		when(noRollupStrategy.getName()).thenReturn(CUSTOM_ROLLUP_STRATEGY);
		when(noRollupStrategy.getOrder()).thenReturn(NO_ROLLUP_SORT_ORDER);
		when(baseProductRollupStrategy.getName()).thenReturn(BASE_PRODUCT_ROLLUP_STRATEGY_NAME);
		when(baseProductRollupStrategy.getOrder()).thenReturn(BASE_PRODUCT_ROLLUP_SORT_ORDER);
		when(customRollupStrategy.getName()).thenReturn(CUSTOM_ROLLUP_STRATEGY);
		when(customRollupStrategy.getOrder()).thenReturn(CUSTOM_ROLLUP_SORT_ORDER);

		final List<MerchandisingMetricRollupStrategy> rollupStrategies = Arrays.asList(customRollupStrategy,
			noRollupStrategy,
			customRollupStrategy);

		assertThatExceptionOfType(MerchandisingMetricRollupException.class)
			.isThrownBy(() -> merchandisingRollupStrategySearchFacade.setRollupStrategies(rollupStrategies))
			.withMessageContaining(DUPLICATE_BEANS_WITH_NAME_EXCEPTION_MESSAGE);
	}

	@Test
	public void testSetupRollupStrategy() throws MerchandisingMetricRollupException
	{
		when(noRollupStrategy.getName()).thenReturn(NO_ROLLUP_STRATEGY_NAME);
		when(noRollupStrategy.getOrder()).thenReturn(NO_ROLLUP_SORT_ORDER);
		when(baseProductRollupStrategy.getName()).thenReturn(BASE_PRODUCT_ROLLUP_STRATEGY_NAME);
		when(baseProductRollupStrategy.getOrder()).thenReturn(BASE_PRODUCT_ROLLUP_SORT_ORDER);
		when(customRollupStrategy.getName()).thenReturn(CUSTOM_ROLLUP_STRATEGY);
		when(customRollupStrategy.getOrder()).thenReturn(CUSTOM_ROLLUP_SORT_ORDER);

		final List<MerchandisingMetricRollupStrategy> rollupStrategies = Arrays.asList(baseProductRollupStrategy,
			customRollupStrategy,
			noRollupStrategy);

		merchandisingRollupStrategySearchFacade.setRollupStrategies(rollupStrategies);

		assertThat(merchandisingRollupStrategySearchFacade.getRollupStrategies()).containsExactly(
			NO_ROLLUP_STRATEGY_NAME,
			BASE_PRODUCT_ROLLUP_STRATEGY_NAME,
			CUSTOM_ROLLUP_STRATEGY);
	}
}