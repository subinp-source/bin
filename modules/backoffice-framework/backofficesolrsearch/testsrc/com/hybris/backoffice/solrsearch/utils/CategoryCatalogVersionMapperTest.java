/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.solrsearch.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CategoryCatalogVersionMapperTest
{
	private static final String CODE = "code";
	private static final String VERSION = "version";
	private static final String CATALOG = "catalog";

	private CategoryCatalogVersionMapper mapper;

	@Before
	public void setUp()
	{
		mapper = new CategoryCatalogVersionMapper();
	}

	@Test
	public void shouldEncodeCategoryModel()
	{
		//given
		final CategoryModel categoryModel = mock(CategoryModel.class);
		doReturn(CODE).when(categoryModel).getCode();

		final CatalogVersionModel catalogVersionModel = mock(CatalogVersionModel.class);
		doReturn(catalogVersionModel).when(categoryModel).getCatalogVersion();
		doReturn(VERSION).when(catalogVersionModel).getVersion();

		final CatalogModel catalogModel = mock(CatalogModel.class);
		doReturn(catalogModel).when(catalogVersionModel).getCatalog();
		doReturn(CATALOG).when(catalogModel).getId();

		//when
		final String encoded = mapper.encode(categoryModel);

		//then
		assertThat(encoded).isEqualTo(CODE + "@@" + CATALOG + "@@" + VERSION);
	}

	@Test
	public void shouldDecode()
	{
		final CategoryCatalogVersionMapper.CategoryWithCatalogVersion encoded = mapper.decode(CODE + "@@" + CATALOG + "@@" + VERSION);

		assertThat(encoded.categoryCode).isEqualTo(CODE);
		assertThat(encoded.catalogId).isEqualTo(CATALOG);
		assertThat(encoded.catalogVersion).isEqualTo(VERSION);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionWhenNoDelimiterIsUsed()
	{
		mapper.decode("noDelimiter");
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionWhenWrongNumberDelimiterIsUsed()
	{
		mapper.decode("too@@many@@delimiter@@are@@used");
	}
}
