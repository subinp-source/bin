/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.validation;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.validation.exceptions.HybrisConstraintViolation;
import de.hybris.platform.validation.model.constraints.jsr303.PatternConstraintModel;
import de.hybris.platform.validation.services.ValidationService;

import javax.annotation.Resource;

import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class PatternConstraintValidationTest extends ServicelayerTransactionalTest
{

	@Resource
	private ModelService modelService;

	@Resource
	private ValidationService validationService;

	@Before
	public void setUp() throws Exception
	{

		createCoreData();
		importCsv("/impex/essentialdata-pattern-constraints.impex", "UTF-8");
		validationService.reloadValidationEngine();
	}

	private <M extends ItemModel> void testConstraints(final M model)
	{
		final Set<HybrisConstraintViolation> validations = validationService.validate(model);
		Assert.assertTrue("The violation set should not be null", validations != null);
		Assert.assertEquals("There should be one violation of constraints", 1, validations.size());
		for (final HybrisConstraintViolation validation : validations)
		{
			Assert.assertEquals("The constraint class should be PatternConstraintModel", PatternConstraintModel.class,
					validation.getConstraintModel().getClass());
		}
	}

	private ProductModel createProduct(final String code)
	{
		final ProductModel productModel = modelService.create(ProductModel.class);
		productModel.setCode(code);
		return productModel;
	}

	private CategoryModel createCategory(final String code)
	{
		final CategoryModel categoryModel = modelService.create(CategoryModel.class);
		categoryModel.setCode(code);
		return categoryModel;
	}

	@Test
	public void testCategorySlashPatternConstraint()
	{
		final CategoryModel model = createCategory("Slash/Category");
		testConstraints(model);
	}

	@Test
	public void testCategoryBackSlashPatternConstraint()
	{
		final CategoryModel model = createCategory("Backslash\\Category");
		testConstraints(model);
	}

	@Test
	public void testCategoryEncodedSlashPatternConstraint()
	{
		final CategoryModel model = createCategory("EncodedSlash%2FCategory");
		testConstraints(model);
	}

	@Test
	public void testCategoryEncodedBackSlashPatternConstraint()
	{
		final CategoryModel model = createCategory("EncodedSlash%5CCategory");
		testConstraints(model);
	}

	@Test
	public void testCategoryEncodedSlashLowerCasePatternConstraint()
	{
		final CategoryModel model = createCategory("EncodedSlash%2fCategory");
		testConstraints(model);
	}

	@Test
	public void testCategoryEncodedBackSlashLowerCasePatternConstraint()
	{
		final CategoryModel model = createCategory("EncodedSlash%5cCategory");
		testConstraints(model);
	}

	@Test
	public void testProductSlashPatternConstraint()
	{
		final ProductModel model = createProduct("Slash/Category");
		testConstraints(model);
	}

	@Test
	public void testProductBackSlashPatternConstraint()
	{
		final ProductModel model = createProduct("Backslash\\Category");
		testConstraints(model);
	}

	@Test
	public void testProductEncodedSlashPatternConstraint()
	{
		final ProductModel model = createProduct("EncodedSlash%2FCategory");
		testConstraints(model);
	}

	@Test
	public void testProductEncodedBackSlashPatternConstraint()
	{
		final ProductModel model = createProduct("EncodedSlash%5CCategory");
		testConstraints(model);
	}

	@Test
	public void testProductEncodedSlashLowerCasePatternConstraint()
	{
		final ProductModel model = createProduct("EncodedSlash%2fCategory");
		testConstraints(model);
	}

	@Test
	public void testProductEncodedBackSlashLowerCasePatternConstraint()
	{
		final ProductModel model = createProduct("EncodedSlash%5cCategory");
		testConstraints(model);
	}
}
