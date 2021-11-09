/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.search.ProductSearchStrategy;
import de.hybris.platform.commerceservices.search.exceptions.ProductSearchStrategyRuntimeException;
import de.hybris.platform.commerceservices.search.solrfacetsearch.SolrFacetSearchProductSearchStrategy;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@UnitTest
public class DefaultProductSearchStrategyFactoryTest
{
	private static final String PRODUCT_SEARCH_STRATEGY_BEAN_NAME = "productSearchStrategyName";

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Mock
	private ApplicationContext applicationContext;

	@Mock
	private BaseStoreService baseStoreService;

	@Mock
	private ProductSearchStrategy productSearchStrategy;

	@Mock
	private SolrFacetSearchProductSearchStrategy solrFacetSearchProductSearchStrategy;

	private DefaultProductSearchStrategyFactory defaultProductSearchStrategyFactory;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		defaultProductSearchStrategyFactory = new DefaultProductSearchStrategyFactory();
		defaultProductSearchStrategyFactory.setBaseStoreService(baseStoreService);
		defaultProductSearchStrategyFactory.setApplicationContext(applicationContext);
	}

	@Test
	public void testDefinedSearchStrategy()
	{
		// given
		final ProductSearchStrategy expectedProductSearchStrategy = productSearchStrategy;
		final BaseStoreModel baseStoreModel = new BaseStoreModel();
		baseStoreModel.setProductSearchStrategy(PRODUCT_SEARCH_STRATEGY_BEAN_NAME);

		when(baseStoreService.getCurrentBaseStore()).thenReturn(baseStoreModel);
		when(applicationContext.getBean(PRODUCT_SEARCH_STRATEGY_BEAN_NAME, ProductSearchStrategy.class))
				.thenReturn(expectedProductSearchStrategy);

		// when
		final ProductSearchStrategy searchStrategy = defaultProductSearchStrategyFactory.getSearchStrategy();

		// then
		assertSame("Search strategy from baseStore should be provided", expectedProductSearchStrategy, searchStrategy);
		verify(applicationContext, Mockito.times(1)).getBean(PRODUCT_SEARCH_STRATEGY_BEAN_NAME, ProductSearchStrategy.class);
	}

	@Test
	public void testDefaultSearchStrategyWhenNoStrategyHasBeenDefinedForBaseStore()
	{
		// given
		final SolrFacetSearchProductSearchStrategy expectedProductSearchStrategy = solrFacetSearchProductSearchStrategy;
		final BaseStoreModel baseStoreModel = new BaseStoreModel();
		defaultProductSearchStrategyFactory.setDefaultProductSearchStrategy(solrFacetSearchProductSearchStrategy);

		when(baseStoreService.getCurrentBaseStore()).thenReturn(baseStoreModel);

		// when
		final ProductSearchStrategy searchStrategy = defaultProductSearchStrategyFactory.getSearchStrategy();

		// then
		assertSame("Default product search strategy should be provided", expectedProductSearchStrategy, searchStrategy);
		verify(applicationContext, Mockito.never()).getBean(PRODUCT_SEARCH_STRATEGY_BEAN_NAME, ProductSearchStrategy.class);
	}

	@Test
	public void testExpectExceptionWhenNoDefaultProductStrategyHasBeenDefined()
	{
		// given
		final BaseStoreModel baseStoreModel = new BaseStoreModel();

		when(baseStoreService.getCurrentBaseStore()).thenReturn(baseStoreModel);

		// expect
		expectedException.expect(ProductSearchStrategyRuntimeException.class);
		expectedException.expectMessage("Cannot retrieve default search product strategy");

		// when
		defaultProductSearchStrategyFactory.getSearchStrategy();
	}

	@Test
	public void testExpectExceptionWhenCurrentBaseStoreIsNull()
	{
		// given
		when(baseStoreService.getCurrentBaseStore()).thenReturn(null);

		// expect
		expectedException.expect(ProductSearchStrategyRuntimeException.class);
		expectedException.expectMessage("The base store is null");

		// when
		defaultProductSearchStrategyFactory.getSearchStrategy();
	}
}
