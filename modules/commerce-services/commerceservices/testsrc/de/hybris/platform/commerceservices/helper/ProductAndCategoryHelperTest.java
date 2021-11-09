/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.helper;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.category.model.ConfigurationCategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.variants.model.VariantCategoryModel;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.variants.model.VariantValueCategoryModel;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class ProductAndCategoryHelperTest
{
	ProductAndCategoryHelper productAndCategoryHelper;

	private VariantProductModel product;

	private VariantProductModel baseProduct1;

	private ProductModel baseProduct2;

	private List<Class<? extends CategoryModel>> productCategoryBlacklist;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		productAndCategoryHelper = new ProductAndCategoryHelper();
		productCategoryBlacklist = new ArrayList<Class<? extends CategoryModel>>();

		productCategoryBlacklist.add(ClassificationClassModel.class);
		productCategoryBlacklist.add(VariantCategoryModel.class);
		productCategoryBlacklist.add(VariantValueCategoryModel.class);
		productCategoryBlacklist.add(ConfigurationCategoryModel.class);

		product = new VariantProductModel();
		baseProduct1 = new VariantProductModel();
		baseProduct2 = new ProductModel();

		productAndCategoryHelper.setProductCategoryBlacklist(productCategoryBlacklist);
	}

	@Test
	public void testValidCategory()
	{
		final CategoryModel category = Mockito.mock(CategoryModel.class);
		Assert.assertTrue(productAndCategoryHelper.isValidProductCategory(category));
	}

	@Test
	public void testInvalidCategory()
	{
		final VariantValueCategoryModel category = Mockito.mock(VariantValueCategoryModel.class);
		Assert.assertFalse(productAndCategoryHelper.isValidProductCategory(category));
	}

	@Test
	public void testNullCategory()
	{
		Assert.assertFalse(productAndCategoryHelper.isValidProductCategory(null));
	}

	@Test
	public void testGetBaseProduct()
	{
		product.setBaseProduct(baseProduct1);
		baseProduct1.setBaseProduct(baseProduct2);
		Assert.assertEquals(baseProduct2, productAndCategoryHelper.getBaseProduct(product));
	}

	@Test
	public void testGetVariantBaseProduct()
	{
		product.setBaseProduct(baseProduct1);
		Assert.assertEquals(baseProduct1, productAndCategoryHelper.getBaseProduct(product));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetBaseProductNULL()
	{
		Assert.assertEquals(baseProduct1, productAndCategoryHelper.getBaseProduct(null));
	}


}
