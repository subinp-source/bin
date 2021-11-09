/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.populators;

import static de.hybris.platform.cms2lib.model.components.ProductCarouselComponentModel.PRODUCTCODES;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2lib.model.components.ProductCarouselComponentModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ProductCarouselComponentModelToDataPopulatorTest
{
	private final String PRODUCT_CODE1 = "productCode1";
	private final String PRODUCT_CODE2 = "productCode2";

	@Mock
	private ProductModel productModel1;
	@Mock
	private ProductModel productModel2;

	@Mock
	private Predicate<CMSItemModel> productCarouselComponentPredicate;

	@Mock
	private ProductCarouselComponentModel itemModel;

	@InjectMocks
	private ProductCarouselComponentModelToDataPopulator productCarouselComponentModelToDataPopulator;

	@Mock
	private Map<String, Object> stringObjectMap;

	List<String> productCodes;
	List<ProductModel> products;

	@Before
	public void setUp()
	{
		productCodes = Arrays.asList(PRODUCT_CODE1, PRODUCT_CODE2);
		products = Arrays.asList(productModel1, productModel2);
		when(productModel1.getCode()).thenReturn(PRODUCT_CODE1);
		when(productModel2.getCode()).thenReturn(PRODUCT_CODE2);
		when(productCarouselComponentPredicate.test(itemModel)).thenReturn(true);
		when(stringObjectMap.get(PRODUCTCODES)).thenReturn(null);
	}

	@Test
	public void shouldNotPopulateProductCodesIfItemIsNotProductCarousel()
	{
		// GIVEN
		when(productCarouselComponentPredicate.test(itemModel)).thenReturn(false);

		// WHEN
		productCarouselComponentModelToDataPopulator.populate(itemModel, stringObjectMap);

		// THEN
		verify(stringObjectMap, never()).put(anyString(), anyObject());
	}

	@Test
	public void shouldPopulateProductCodesIfProductCodesMethodReturnsNonNullValue()
	{
		// GIVEN

		when(itemModel.getProductCodes()).thenReturn(productCodes);

		// WHEN
		productCarouselComponentModelToDataPopulator.populate(itemModel, stringObjectMap);

		// THEN
		verify(stringObjectMap, times(1)).put(PRODUCTCODES, productCodes);
	}

	@Test
	public void shouldPopulateProductCodesIfProductsMethodReturnsNonNullValue()
	{
		// GIVEN
		when(itemModel.getProductCodes()).thenReturn(null);
		when(itemModel.getProducts()).thenReturn(products);


		// WHEN
		productCarouselComponentModelToDataPopulator.populate(itemModel, stringObjectMap);

		// THEN
		verify(stringObjectMap, times(1)).put(PRODUCTCODES, productCodes);
	}

	@Test
	public void shouldNotPopulateProductCodesIfProductCodesAndProductsMethodsReturnNullValue()
	{
		// GIVEN
		when(itemModel.getProductCodes()).thenReturn(null);
		when(itemModel.getProducts()).thenReturn(null);

		// WHEN
		productCarouselComponentModelToDataPopulator.populate(itemModel, stringObjectMap);

		// THEN
		verify(stringObjectMap, never()).put(anyString(), anyObject());
	}
}
