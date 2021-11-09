/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.solrsearch.resolvers;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.solrsearch.providers.ProductCategoryAssignmentResolver;
import com.hybris.backoffice.solrsearch.utils.CategoryCatalogVersionMapper;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CategoryCodeWithCatalogVersionMappingValueResolverTest
{
	@InjectMocks
	private CategoryCodeWithCatalogVersionMappingValueResolver resolver;

	@Mock
	private ProductCategoryAssignmentResolver valueProvider;
	@Mock
	private CategoryCatalogVersionMapper categoryCatalogVersionMapper;

	@Test
	public void shouldAddEncodedCategoryToDocument() throws FieldValueProviderException
	{
		final ProductModel product = mock(ProductModel.class);
		final Set categories = new LinkedHashSet();
		final CategoryModel classA = mock(CategoryModel.class);
		categories.add(classA);
		when(classA.getCode()).thenReturn("cat1");
		final CategoryModel classB = mock(CategoryModel.class);
		categories.add(classB);
		when(classB.getCode()).thenReturn("cat2");
		doReturn(categories).when(valueProvider).getIndexedCategories(product);

		doReturn("cat1@@default@@staged").when(categoryCatalogVersionMapper).encode(classA);
		doReturn("cat2@@default@@staged").when(categoryCatalogVersionMapper).encode(classB);

		//when
		final IndexedProperty property = mock(IndexedProperty.class);
		final InputDocument document = mock(InputDocument.class);
		resolver.addFieldValues(document, null, property, product, null);

		//then
		verify(document).addField(property, "cat1@@default@@staged");
		verify(document).addField(property, "cat2@@default@@staged");
	}
}
