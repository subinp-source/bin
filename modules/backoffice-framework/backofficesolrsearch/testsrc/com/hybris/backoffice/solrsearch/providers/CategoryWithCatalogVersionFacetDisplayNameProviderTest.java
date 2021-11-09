/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.solrsearch.providers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.proxy.LabelServiceProxy;
import com.hybris.backoffice.solrsearch.utils.CategoryCatalogVersionMapper;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CategoryWithCatalogVersionFacetDisplayNameProviderTest
{

	private static final String CATEGORY_CODE = "category";
	private static final String CATALOG_NAME = "Default";
	private static final String CATALOG_VERSION_NAME = "Staged";
	private static final String NAME = CATEGORY_CODE + "@@" + CATALOG_NAME + "@@" + CATALOG_VERSION_NAME;
	private static final String LANGUAGE_EN = "en";

	@InjectMocks
	private CategoryWithCatalogVersionFacetDisplayNameProvider provider;
	@Mock
	private CategoryCatalogVersionMapper categoryCatalogVersionMapper;
	@Mock
	private CatalogVersionService catalogVersionService;
	@Mock
	private CategoryService categoryService;
	@Mock
	private LabelServiceProxy labelServiceProxy;


	@Test
	public void shouldReturnCategoryName()
	{
		//given
		final SearchQuery query = mock(SearchQuery.class);
		doReturn(LANGUAGE_EN).when(query).getLanguage();

		final CategoryCatalogVersionMapper.CategoryWithCatalogVersion decoded = new CategoryCatalogVersionMapper.CategoryWithCatalogVersion(
				CATEGORY_CODE, CATALOG_NAME, CATALOG_VERSION_NAME);
		doReturn(decoded).when(categoryCatalogVersionMapper).decode(NAME);

		final CatalogVersionModel catalogVersionMock = mock(CatalogVersionModel.class);
		doReturn(catalogVersionMock).when(catalogVersionService).getCatalogVersion(CATALOG_NAME, CATALOG_VERSION_NAME);
		final CategoryModel categoryMock = mock(CategoryModel.class);
		doReturn(categoryMock).when(categoryService).getCategoryForCode(catalogVersionMock, CATEGORY_CODE);

		//when
		provider.getDisplayName(query, NAME);

		//then
		verify(labelServiceProxy).getObjectLabel(eq(categoryMock), any());
	}

	@Test
	public void shouldReturnNameWhenExceptionIsThrownOnGettingLabelForName()
	{
		//given
		final String name = NAME;
		final SearchQuery query = mock(SearchQuery.class);
		doReturn(LANGUAGE_EN).when(query).getLanguage();

		final CategoryCatalogVersionMapper.CategoryWithCatalogVersion decoded = new CategoryCatalogVersionMapper.CategoryWithCatalogVersion(
				CATEGORY_CODE, CATALOG_NAME, CATALOG_VERSION_NAME);
		doReturn(decoded).when(categoryCatalogVersionMapper).decode(name);

		doThrow(RuntimeException.class).when(catalogVersionService).getCatalogVersion(any(), any());

		//when
		final String displayName = provider.getDisplayName(query, name);

		//then
		assertThat(displayName).isEqualTo(name);
	}

}
