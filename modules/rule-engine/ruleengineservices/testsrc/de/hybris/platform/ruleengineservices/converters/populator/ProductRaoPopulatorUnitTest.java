/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.ruleengineservices.converters.populator;

import static de.hybris.platform.ruleengineservices.util.TestUtil.createNewConverter;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.impl.DefaultCategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ruleengineservices.rao.CategoryRAO;
import de.hybris.platform.ruleengineservices.rao.ProductRAO;
import de.hybris.platform.ruleengineservices.util.ProductUtils;
import de.hybris.platform.variants.model.GenericVariantProductModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Sets;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ProductRaoPopulatorUnitTest
{
	@InjectMocks
	private ProductRaoPopulator populator;
	@Mock
	private ProductUtils productUtils;

	@Before
	public void setUp() throws Exception
	{
		populator.setCategoryConverter(createNewConverter(CategoryRAO.class, new CategoryRaoPopulator()));
		final CategoryService catService = new DefaultCategoryService()
		{
			@Override
			public Collection<CategoryModel> getAllSupercategoriesForCategory(final CategoryModel category)
			{
				final CategoryModel categoryModel = new CategoryModel();
				categoryModel.setCode("super" + category.getCode());
				return Collections.singletonList(categoryModel);
			}
		};
		populator.setCategoryService(catService);
	}

	protected Collection<CategoryModel> getCategories(final String... categoryCodes)
	{
		final Collection<CategoryModel> categories = new ArrayList<>();
		for (final String code : Arrays.asList(categoryCodes))
		{
			final CategoryModel cat = new CategoryModel();
			cat.setCode(code);
			categories.add(cat);
		}
		return categories;
	}

	@Test
	public void testProductModelPopulating()
	{
		final ProductModel productModel = new ProductModel();
		productModel.setCode("prod1");
		productModel.setSupercategories(getCategories("cat1", "cat2"));
		final ProductRAO productRao = new ProductRAO();
		populator.populate(productModel, productRao);
		Assert.assertEquals(productModel.getCode(), productRao.getCode());
	}

	@Test
	public void testGenericVariantProductModelPopulating()
	{
		final GenericVariantProductModel genericVariantProductModel = new GenericVariantProductModel();
		genericVariantProductModel.setCode("code2");
		genericVariantProductModel.setSupercategories(getCategories("cat1", "cat2"));
		final ProductModel productModel = new ProductModel();
		productModel.setSupercategories(getCategories("cat3", "cat4"));
		genericVariantProductModel.setBaseProduct(productModel);

		when(productUtils.getAllBaseProducts(genericVariantProductModel)).thenReturn(Sets.newHashSet(productModel));

		final ProductRAO productRao = new ProductRAO();
		populator.populate(genericVariantProductModel, productRao);
		Assert.assertEquals(genericVariantProductModel.getCode(), productRao.getCode());
	}

	@Test
	public void testProductModelPopulatingNoCategories()
	{
		final ProductModel productModel = new ProductModel();
		productModel.setCode("prod1");
		final ProductRAO productRao = new ProductRAO();
		populator.populate(productModel, productRao);
		Assert.assertEquals(productModel.getCode(), productRao.getCode());
	}

	@Test
	public void testGenericVariantProductModelNoCategoriesPopulating()
	{
		final GenericVariantProductModel genericVariantProductModel = new GenericVariantProductModel();
		genericVariantProductModel.setCode("code2");
		final ProductModel productModel = new ProductModel();
		genericVariantProductModel.setBaseProduct(productModel);
		final ProductRAO productRao = new ProductRAO();
		populator.populate(genericVariantProductModel, productRao);
		Assert.assertEquals(genericVariantProductModel.getCode(), productRao.getCode());
	}
}
