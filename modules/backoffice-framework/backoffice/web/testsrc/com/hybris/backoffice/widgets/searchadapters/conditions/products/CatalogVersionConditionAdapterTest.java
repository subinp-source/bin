/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.searchadapters.conditions.products;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.product.ProductModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;


@RunWith(MockitoJUnitRunner.class)
public class CatalogVersionConditionAdapterTest
{

	private CatalogVersionConditionAdapter catalogVersionConditionAdapter;

	@Before
	public void setup()
	{
		catalogVersionConditionAdapter = new CatalogVersionConditionAdapter();
		catalogVersionConditionAdapter.setCatalogVersionPropertyName(ProductModel.CATALOGVERSION);
		catalogVersionConditionAdapter.setOperator(ValueComparisonOperator.EQUALS);
	}

	@Test
	public void shouldAddConditionsForCatalogVersion()
	{
		// given
		final AdvancedSearchData searchData = new AdvancedSearchData();
		final NavigationNode navigationNode = mock(NavigationNode.class);
		final CatalogVersionModel catalogVersion = mock(CatalogVersionModel.class);
		final PK catalogVersionPK = PK.BIG_PK;

		given(navigationNode.getData()).willReturn(catalogVersion);
		given(catalogVersion.getPk()).willReturn(catalogVersionPK);

		// when
		catalogVersionConditionAdapter.addSearchCondition(searchData, navigationNode);

		// then
		final SearchConditionData searchConditionData = searchData.getCondition(0);
		assertThat(searchConditionData.getFieldType().getName()).isEqualTo(ProductModel.CATALOGVERSION);
		assertThat(searchConditionData.getValue()).isEqualTo(catalogVersionPK);
		assertThat(searchConditionData.getOperator()).isEqualTo(ValueComparisonOperator.EQUALS);
	}
}
