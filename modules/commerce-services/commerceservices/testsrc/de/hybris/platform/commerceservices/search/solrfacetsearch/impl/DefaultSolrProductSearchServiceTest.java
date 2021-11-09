/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.solrfacetsearch.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.search.exceptions.ProductSearchStrategyRuntimeException;
import de.hybris.platform.commerceservices.search.impl.DefaultProductSearchStrategyFactory;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.exceptions.NoValidSolrConfigException;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import static org.mockito.Mockito.when;


@UnitTest
public class DefaultSolrProductSearchServiceTest
{
    private static final String CANNOT_RETRIEVE_DEFAULT_SEARCH_PRODUCT_STRATEGY_MESSAGE = "Cannot retrieve default search product strategy";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private BaseStoreService baseStoreService;

    @Mock
    private ApplicationContext applicationContext;

    private DefaultSolrProductSearchService<SearchResultValueData> productSearchService;
    private DefaultProductSearchStrategyFactory productSearchStrategyFactory;

    @Before
    public void setUp() throws FacetConfigServiceException, NoValidSolrConfigException
    {
        MockitoAnnotations.initMocks(this);

        productSearchStrategyFactory = new DefaultProductSearchStrategyFactory();
        productSearchStrategyFactory.setBaseStoreService(baseStoreService);
        productSearchStrategyFactory.setApplicationContext(applicationContext);

        productSearchService = new DefaultSolrProductSearchService<SearchResultValueData>();
        productSearchService.setProductSearchStrategyFactory(productSearchStrategyFactory);

    }

    @Test
    public void testExpectExceptionWhenNoDefaultProductStrategyHasBeenDefinedForTextSearch()
    {
        // given
        final BaseStoreModel baseStoreModel = new BaseStoreModel();
        when(baseStoreService.getCurrentBaseStore()).thenReturn(baseStoreModel);

        // expect
        expectedException.expect(ProductSearchStrategyRuntimeException.class);
        expectedException.expectMessage(CANNOT_RETRIEVE_DEFAULT_SEARCH_PRODUCT_STRATEGY_MESSAGE);

        // when
        productSearchService.textSearch("", null);
    }

    @Test
    public void testExpectExceptionWhenNoDefaultProductStrategyHasBeenDefinedForTextSearchWithSQC()
    {
        // given
        final BaseStoreModel baseStoreModel = new BaseStoreModel();
        when(baseStoreService.getCurrentBaseStore()).thenReturn(baseStoreModel);

        // expect
        expectedException.expect(ProductSearchStrategyRuntimeException.class);
        expectedException.expectMessage(CANNOT_RETRIEVE_DEFAULT_SEARCH_PRODUCT_STRATEGY_MESSAGE);

        // when
        productSearchService.textSearch("", null, null);
    }

    @Test
    public void testExpectExceptionWhenNoDefaultProductStrategyHasBeenDefinedForCategorySearch()
    {
        // given
        final BaseStoreModel baseStoreModel = new BaseStoreModel();
        when(baseStoreService.getCurrentBaseStore()).thenReturn(baseStoreModel);

        // expect
        expectedException.expect(ProductSearchStrategyRuntimeException.class);
        expectedException.expectMessage(CANNOT_RETRIEVE_DEFAULT_SEARCH_PRODUCT_STRATEGY_MESSAGE);

        // when
        productSearchService.categorySearch("", null);
    }

    @Test
    public void testExpectExceptionWhenNoDefaultProductStrategyHasBeenDefinedForCategorySearchWithSQC()
    {
        // given
        final BaseStoreModel baseStoreModel = new BaseStoreModel();
        when(baseStoreService.getCurrentBaseStore()).thenReturn(baseStoreModel);

        // expect
        expectedException.expect(ProductSearchStrategyRuntimeException.class);
        expectedException.expectMessage(CANNOT_RETRIEVE_DEFAULT_SEARCH_PRODUCT_STRATEGY_MESSAGE);

        // when
        productSearchService.categorySearch("", null, null);
    }

    @Test
    public void testExpectExceptionWhenNoDefaultProductStrategyHasBeenDefinedForSearchAgain()
    {
        // given
        final BaseStoreModel baseStoreModel = new BaseStoreModel();
        when(baseStoreService.getCurrentBaseStore()).thenReturn(baseStoreModel);

        // expect
        expectedException.expect(ProductSearchStrategyRuntimeException.class);
        expectedException.expectMessage(CANNOT_RETRIEVE_DEFAULT_SEARCH_PRODUCT_STRATEGY_MESSAGE);

        // when
        productSearchService.searchAgain(null, null);
    }
}
