/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ruleengineservices.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.Set;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ProductToCategoryCodesMapperTest
{
	@Mock
	private ProductModel product;

	@InjectMocks
	private ProductToCategoryCodesMapper mapper;

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIEAOnNullParameter()
	{
		mapper.apply(null);
	}

	@Test(expected = NullPointerException.class)
	public void shouldThrowNPEOnNullSupercategories()
	{
		when(product.getSupercategories()).thenReturn(null);
		mapper.apply(product);
	}

	@Test
	public void shouldResultInEmptySetOnEmptySupercategories()
	{
		when(product.getSupercategories()).thenReturn(Collections.emptyList());
		final Set<String> result = mapper.apply(product);
		assertThat(result).isEmpty();
	}

	@Test
	public void shouldMapCategoryCodesCorrectly()
	{
		final CategoryModel category1 = mock(CategoryModel.class);
		when(category1.getCode()).thenReturn("category1");

		final CategoryModel category2 = mock(CategoryModel.class);
		when(category2.getCode()).thenReturn("category2");

		final CategoryModel category3 = mock(CategoryModel.class);
		when(category3.getCode()).thenReturn("category3");

		final CategoryModel category4 = mock(CategoryModel.class);
		when(category4.getCode()).thenReturn("category1");

		when(product.getSupercategories()).thenReturn(List.of(category1, category2, category3, category4));
		final Set<String> result = mapper.apply(product);

		assertThat(result).isNotEmpty().containsExactlyInAnyOrder("category1", "category2", "category3");
	}
}
