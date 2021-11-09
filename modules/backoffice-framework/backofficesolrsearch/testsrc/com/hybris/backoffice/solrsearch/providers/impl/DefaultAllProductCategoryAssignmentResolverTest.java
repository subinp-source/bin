/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.solrsearch.providers.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class DefaultAllProductCategoryAssignmentResolverTest
{

	@Spy
	private DefaultAllProductCategoryAssignmentResolver resolver;

	@Test
	public void getIndexedCategories()
	{
		//given
		final ProductModel product = mock(ProductModel.class);
		final CategoryModel catA = mock(CategoryModel.class);
		final Set<CategoryModel> assignments = Collections.singleton(catA);
		doReturn(assignments).when(product).getSupercategories();

		//when
		resolver.getIndexedCategories(product);

		//then
		final ArgumentCaptor<Collection> captor = ArgumentCaptor.forClass(Collection.class);
		verify(resolver).resolveSuperCategories(captor.capture());
		assertThat(captor.getValue()).contains(catA);
	}

	@Test
	public void resolveSuperCategories()
	{
		//given
		final CategoryModel catA = mock(CategoryModel.class);
		final CategoryModel catA1 = mock(CategoryModel.class);
		doReturn(Collections.singletonList(catA1)).when(catA).getAllSupercategories();
		final CategoryModel catB = mock(CategoryModel.class);
		final CategoryModel catB1 = mock(CategoryModel.class);
		final CategoryModel catB2 = mock(CategoryModel.class);
		doReturn(List.of(catB1, catB2)).when(catB).getAllSupercategories();

		//when
		final Collection<CategoryModel> res = resolver.resolveSuperCategories(List.of(catA, catB));

		//then
		assertThat(res).containsAll(List.of(catA1, catB1, catB2));
	}
}
