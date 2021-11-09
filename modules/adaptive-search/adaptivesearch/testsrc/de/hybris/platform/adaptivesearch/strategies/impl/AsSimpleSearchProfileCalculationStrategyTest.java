/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.strategies.impl;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.data.AsConfigurableSearchConfiguration;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;
import de.hybris.platform.adaptivesearch.data.AsSimpleSearchProfile;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProfileResultFactory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class AsSimpleSearchProfileCalculationStrategyTest
{
	private AsSimpleSearchProfileCalculationStrategy strategy;

	@Mock
	private AsSearchProfileResultFactory asSearchProfileResultFactory;

	@Mock
	private AsSearchProfileContext context;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		strategy = new AsSimpleSearchProfileCalculationStrategy();
		strategy.setAsSearchProfileResultFactory(asSearchProfileResultFactory);
	}

	@Test
	public void calculate()
	{
		//given
		final AsSimpleSearchProfile searchProfileData = new AsSimpleSearchProfile();
		final AsSearchProfileResult searchProfileResultData = new AsSearchProfileResult();

		final AsConfigurableSearchConfiguration searchConfigurationData = new AsConfigurableSearchConfiguration();
		searchProfileData.setSearchConfiguration(searchConfigurationData);

		when(asSearchProfileResultFactory.createResultFromSearchConfiguration(searchConfigurationData))
				.thenReturn(searchProfileResultData);

		//when
		final AsSearchProfileResult calculatedSearchProfileResult = strategy.calculate(context, searchProfileData);

		//then
		assertSame(calculatedSearchProfileResult, searchProfileResultData);
	}
}
