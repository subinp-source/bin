/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ruleengineservices.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.variants.model.VariantProductModel;
import org.junit.Before;
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
public class ProductToProductCodesMapperTest
{
	@Mock
	private ProductModel product;

	@InjectMocks
	private ProductToProductCodesMapper mapper;

	@Before
	public void setUp()
	{
		when(product.getCode()).thenReturn("productCode");
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIEAOnNullParameter()
	{
		mapper.apply(null);
	}

	@Test
	public void shouldResultInSingleValueSetOnEmptyVariants()
	{
		when(product.getVariants()).thenReturn(Collections.emptyList());
		final Set<String> result = mapper.apply(product);
		assertThat(result).isNotEmpty().containsOnly("productCode");
	}

	@Test
	public void shouldMapProductCodesCorrectly()
	{
		final VariantProductModel product1 = mock(VariantProductModel.class);
		when(product1.getCode()).thenReturn("product1");

		final VariantProductModel product2 = mock(VariantProductModel.class);
		when(product2.getCode()).thenReturn("product2");

		final VariantProductModel product3 = mock(VariantProductModel.class);
		when(product3.getCode()).thenReturn("product3");

		final VariantProductModel product4 = mock(VariantProductModel.class);
		when(product4.getCode()).thenReturn("product1");

		when(product.getVariants()).thenReturn(List.of(product1, product2));
		when(product1.getVariants()).thenReturn(List.of(product3));
		when(product2.getVariants()).thenReturn(List.of(product4));

		final Set<String> result = mapper.apply(product);

		assertThat(result).isNotEmpty().containsExactlyInAnyOrder("productCode", "product1", "product2", "product3");
	}
}
