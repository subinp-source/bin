/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.populators;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ProductPageModel;
import de.hybris.platform.cms2.model.restrictions.CMSProductRestrictionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ProductPageModelToDataRenderingPopulatorTest
{
	@Mock
	private Predicate<ItemModel> productPageTypePredicate;
	@InjectMocks
	private ProductPageModelToDataRenderingPopulator populator;

	@Mock
	private CMSProductRestrictionModel productRestriction1;
	@Mock
	private CMSProductRestrictionModel productRestriction2;
	@Mock
	private ProductModel productA;
	@Mock
	private ProductModel productB;
	@Mock
	private ProductModel productC;

	@Test
	public void shouldPopulateProductCodesForProductPage()
	{
		final Map<String, Object> targetMap = new HashMap<>();
		final ProductPageModel productPage = new ProductPageModel();
		productPage.setRestrictions(Arrays.asList(productRestriction1, productRestriction2));
		doReturn(Boolean.TRUE).when(productPageTypePredicate).test(productPage);
		doReturn(Arrays.asList(productA, productB)).when(productRestriction1).getProducts();
		doReturn(Arrays.asList(productC)).when(productRestriction2).getProducts();
		doReturn("prodA").when(productA).getCode();
		doReturn("prodB").when(productB).getCode();
		doReturn("prodC").when(productC).getCode();

		populator.populate(productPage, targetMap);

		assertThat((Collection<String>) targetMap.get("productCodes"), hasItems("prodA", "prodB", "prodC"));
	}

	@Test
	public void shouldNotPopulateProductCodesForNonProductPage()
	{
		final Map<String, Object> targetMap = new HashMap<>();
		final AbstractPageModel page = new AbstractPageModel();
		doReturn(Boolean.FALSE).when(productPageTypePredicate).test(page);

		populator.populate(page, targetMap);

		assertThat(targetMap.get("productCodes"), nullValue());
	}

}
