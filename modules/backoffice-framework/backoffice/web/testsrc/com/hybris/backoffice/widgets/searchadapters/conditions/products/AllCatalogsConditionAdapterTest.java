/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.searchadapters.conditions.products;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;


@RunWith(MockitoJUnitRunner.class)
public class AllCatalogsConditionAdapterTest
{

	private final AllCatalogsConditionAdapter allCatalogsConditionAdapter = new AllCatalogsConditionAdapter();

	@Test
	public void shouldNotAddConditionsForAllCatalogs()
	{
		// given
		final AdvancedSearchData searchData = mock(AdvancedSearchData.class);
		final NavigationNode navigationNode = mock(NavigationNode.class);

		// when
		allCatalogsConditionAdapter.addSearchCondition(searchData, navigationNode);

		// then
		verifyZeroInteractions(searchData);
	}
}
