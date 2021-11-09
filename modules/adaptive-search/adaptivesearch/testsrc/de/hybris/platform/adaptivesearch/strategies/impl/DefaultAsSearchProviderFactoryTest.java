/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.strategies.impl;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProvider;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;


@UnitTest
public class DefaultAsSearchProviderFactoryTest
{
	private static final String SEARCH_PROVIDER_NAME = "searchProviderName";

	private DefaultAsSearchProviderFactory defaultAsSearchProviderFactory;

	@Mock
	private ApplicationContext applicationContext;

	@Mock
	private AsSearchProvider asSearchProvider;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		defaultAsSearchProviderFactory = new DefaultAsSearchProviderFactory();
		defaultAsSearchProviderFactory.setApplicationContext(applicationContext);
	}

	@Test
	public void testGetSearchProvider() throws Exception
	{
		// given
		final AsSearchProvider expectedSearchProvider = asSearchProvider;

		when(applicationContext.getBeansOfType(AsSearchProvider.class))
				.thenReturn(Collections.singletonMap(SEARCH_PROVIDER_NAME, expectedSearchProvider));
		when(applicationContext.getBean(SEARCH_PROVIDER_NAME, AsSearchProvider.class)).thenReturn(expectedSearchProvider);

		// when
		final AsSearchProvider actualSearchProvider = defaultAsSearchProviderFactory.getSearchProvider();

		// then
		assertSame(expectedSearchProvider, actualSearchProvider);
	}
}
