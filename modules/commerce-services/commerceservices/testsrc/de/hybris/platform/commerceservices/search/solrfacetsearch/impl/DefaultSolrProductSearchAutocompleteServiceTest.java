/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.solrfacetsearch.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.search.exceptions.ProductSearchStrategyRuntimeException;
import de.hybris.platform.commerceservices.search.impl.DefaultProductSearchStrategyFactory;
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
public class DefaultSolrProductSearchAutocompleteServiceTest
{
    private static final String QUERY = "blue cam";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private BaseStoreService baseStoreService;

    @Mock
    private ApplicationContext applicationContext;

    private DefaultProductSearchStrategyFactory productSearchStrategyFactory;
    private DefaultSolrProductSearchAutocompleteService solrProductSearchAutocompleteService;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);

        productSearchStrategyFactory = new DefaultProductSearchStrategyFactory();
        productSearchStrategyFactory.setBaseStoreService(baseStoreService);
        productSearchStrategyFactory.setApplicationContext(applicationContext);

        solrProductSearchAutocompleteService = new DefaultSolrProductSearchAutocompleteService();
        solrProductSearchAutocompleteService.setProductSearchStrategyFactory(productSearchStrategyFactory);
    }

    @Test
    public void testExpectExceptionWhenNoDefaultProductStrategyHasBeenDefinedForGetAutocompleteSuggestions()
    {
        // given
        final BaseStoreModel baseStoreModel = new BaseStoreModel();
        when(baseStoreService.getCurrentBaseStore()).thenReturn(baseStoreModel);

        // expect
        expectedException.expect(ProductSearchStrategyRuntimeException.class);
        expectedException.expectMessage("Cannot retrieve default search product strategy");

        // when
        solrProductSearchAutocompleteService.getAutocompleteSuggestions(QUERY);
    }
}
