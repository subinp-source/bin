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
package de.hybris.platform.ruledefinitions.conditions;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.ruledefinitions.AmountOperator;
import de.hybris.platform.ruledefinitions.CollectionOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeRelCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrEmptyCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrExistsCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrLocalVariablesContainer;
import de.hybris.platform.ruleengineservices.compiler.RuleIrNotCondition;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.rao.ProductRAO;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class RuleQualifyingCategoriesConditionTranslatorTest
{
	private static final String ORDER_ENTRY_RAO_VAR = "orderEntryRaoVariable";
	private static final String CART_RAO_VAR = "cartRaoVariable";
	private static final String PRODUCT_RAO_VAR = "productRaoVariable";
	private static final String AVAILABLE_QUANTITY_VAR = "availableQuantity";

	public static final String CART_RAO_ENTRIES_ATTRIBUTE = "entries";
	public static final String ORDER_ENTRY_RAO_PRODUCT_CODE_ATTRIBUTE = "productCode";
	public static final String ORDER_ENTRY_RAO_CATEGORY_CODES_ATTRIBUTE = "categoryCodes";
	public static final String BASE_PRODUCT_CODES_ATTRIBUTE = "baseProductCodes";

	@InjectMocks
	private RuleQualifyingCategoriesConditionTranslator translator;

	@Mock
	private RuleCompilerContext context;
	@Mock
	private RuleConditionData condition;
	@Mock
	private RuleConditionDefinitionData conditionDefinition;
	@Mock
	private Map<String, RuleParameterData> parameters;
	@Mock
	private RuleIrLocalVariablesContainer variablesContainer;
	@Mock
	private RuleParameterData operatorParameter, quantityParameter, categoriesOperatorParameter, categoriesParameter,
			excludedCategoriesParameter, excludedProductsParameter;
	@Mock
	private RuleConditionConsumptionSupport consumptionSupport;

	@Before
	public void setUp()
	{
		when(condition.getParameters()).thenReturn(parameters);
		when(parameters.get(RuleQualifyingCategoriesConditionTranslator.OPERATOR_PARAM)).thenReturn(operatorParameter);
		when(parameters.get(RuleQualifyingCategoriesConditionTranslator.QUANTITY_PARAM)).thenReturn(quantityParameter);
		when(parameters.get(RuleQualifyingCategoriesConditionTranslator.CATEGORIES_OPERATOR_PARAM))
				.thenReturn(categoriesOperatorParameter);
		when(parameters.get(RuleQualifyingCategoriesConditionTranslator.CATEGORIES_PARAM)).thenReturn(categoriesParameter);
		when(operatorParameter.getValue()).thenReturn(AmountOperator.GREATER_THAN);
		when(quantityParameter.getValue()).thenReturn(1);
		when(context.createLocalContainer()).thenReturn(variablesContainer);

		final List<String> categoryList = new ArrayList<>();
		categoryList.add("categoryCode1");
		categoryList.add("categoryCode2");
		when(categoriesParameter.getValue()).thenReturn(categoryList);

		when(context.generateVariable(OrderEntryRAO.class)).thenReturn(ORDER_ENTRY_RAO_VAR);
		when(context.generateVariable(CartRAO.class)).thenReturn(CART_RAO_VAR);
		when(context.generateVariable(ProductRAO.class)).thenReturn(PRODUCT_RAO_VAR);
	}

	@Test
	public void testTranslateOperatorParamNull()
	{
		when(parameters.get(RuleQualifyingCategoriesConditionTranslator.OPERATOR_PARAM)).thenReturn(null);
		final RuleIrCondition ruleIrCondition = translator.translate(context, condition, conditionDefinition);
		assertThat(ruleIrCondition, instanceOf(RuleIrEmptyCondition.class));
	}

	@Test
	public void testTranslateCategoriesOperatorParamNull()
	{
		when(parameters.get(RuleQualifyingCategoriesConditionTranslator.CATEGORIES_OPERATOR_PARAM)).thenReturn(null);
		final RuleIrCondition ruleIrCondition = translator.translate(context, condition, conditionDefinition);
		assertThat(ruleIrCondition, instanceOf(RuleIrEmptyCondition.class));
	}

	@Test
	public void testTranslateCategoriesParamNull()
	{
		when(parameters.get(RuleQualifyingCategoriesConditionTranslator.CATEGORIES_PARAM)).thenReturn(null);
		final RuleIrCondition ruleIrCondition = translator.translate(context, condition, conditionDefinition);
		assertThat(ruleIrCondition, instanceOf(RuleIrEmptyCondition.class));
	}

	@Test
	public void testTranslateNotOperatorCondition()
	{
		when(categoriesOperatorParameter.getValue()).thenReturn(CollectionOperator.NOT_CONTAINS);

		final RuleIrCondition ruleIrCondition = translator.translate(context, condition, conditionDefinition);
		assertThat(ruleIrCondition, instanceOf(RuleIrGroupCondition.class));

		final RuleIrGroupCondition irGroupCondition = (RuleIrGroupCondition) ruleIrCondition;
		final List<RuleIrCondition> childCondition = irGroupCondition.getChildren();
		assertThat(childCondition.get(0), instanceOf(RuleIrNotCondition.class));
		final RuleIrNotCondition irNotCondition = (RuleIrNotCondition) childCondition.get(0);
		assertEquals(3, irNotCondition.getChildren().size());
		checkChildConditions(irNotCondition.getChildren(), RuleIrGroupOperator.OR);
	}

	@Test
	public void testTranslateAnyOperatorCondition()
	{
		when(categoriesOperatorParameter.getValue()).thenReturn(CollectionOperator.CONTAINS_ANY);

		final RuleIrCondition ruleIrCondition = translator.translate(context, condition, conditionDefinition);
		assertThat(ruleIrCondition, instanceOf(RuleIrGroupCondition.class));
		final RuleIrGroupCondition irGroupCondition = (RuleIrGroupCondition) ruleIrCondition;
		assertEquals(3, irGroupCondition.getChildren().size());
		checkChildConditions(irGroupCondition.getChildren(), RuleIrGroupOperator.OR);
	}

	@Test
	public void testTranslateAnyOperatorWithExcludeCategoryCondition()
	{
		when(categoriesOperatorParameter.getValue()).thenReturn(CollectionOperator.CONTAINS_ANY);
		when(parameters.get(RuleQualifyingCategoriesConditionTranslator.EXCLUDED_CATEGORIES_PARAM))
				.thenReturn(excludedCategoriesParameter);
		final List<String> excludedCategories = new ArrayList<>();
		excludedCategories.add("excludeSubCat1");
		excludedCategories.add("excludeSubCat2");
		when(excludedCategoriesParameter.getValue()).thenReturn(excludedCategories);

		final RuleIrCondition ruleIrCondition = translator.translate(context, condition, conditionDefinition);
		assertThat(ruleIrCondition, instanceOf(RuleIrGroupCondition.class));
		final RuleIrGroupCondition irGroupCondition = (RuleIrGroupCondition) ruleIrCondition;
		assertEquals(4, irGroupCondition.getChildren().size());
		checkChildConditions(irGroupCondition.getChildren(), RuleIrGroupOperator.OR);
		checkExcludeCategoryCondition(irGroupCondition.getChildren().get(3));
	}


	private void checkExcludeCategoryCondition(final RuleIrCondition ruleIrCondition)
	{
		assertThat(ruleIrCondition, instanceOf(RuleIrGroupCondition.class));
		final RuleIrGroupCondition excludeGroupCondition = (RuleIrGroupCondition) ruleIrCondition;
		assertEquals(2, excludeGroupCondition.getChildren().size());

		assertThat(excludeGroupCondition.getChildren().get(0), instanceOf(RuleIrAttributeCondition.class));
		final RuleIrAttributeCondition ruleIrAttributeCategory1Condition =
				(RuleIrAttributeCondition) excludeGroupCondition.getChildren().get(0);
		assertEquals(RuleIrAttributeOperator.NOT_CONTAINS, ruleIrAttributeCategory1Condition.getOperator());
		assertEquals(ORDER_ENTRY_RAO_CATEGORY_CODES_ATTRIBUTE, ruleIrAttributeCategory1Condition.getAttribute());
		assertEquals("excludeSubCat1", ruleIrAttributeCategory1Condition.getValue());

		assertThat(excludeGroupCondition.getChildren().get(1), instanceOf(RuleIrAttributeCondition.class));
		final RuleIrAttributeCondition ruleIrAttributeCategory2Condition =
				(RuleIrAttributeCondition) excludeGroupCondition.getChildren().get(1);
		assertEquals(RuleIrAttributeOperator.NOT_CONTAINS, ruleIrAttributeCategory2Condition.getOperator());
		assertEquals(ORDER_ENTRY_RAO_CATEGORY_CODES_ATTRIBUTE, ruleIrAttributeCategory2Condition.getAttribute());
		assertEquals("excludeSubCat2", ruleIrAttributeCategory2Condition.getValue());
	}

	@Test
	public void testTranslateAnyOperatorWithExcludeProductsCondition()
	{
		when(categoriesOperatorParameter.getValue()).thenReturn(CollectionOperator.CONTAINS_ANY);
		when(parameters.get(RuleQualifyingCategoriesConditionTranslator.EXCLUDED_PRODUCTS_PARAM))
				.thenReturn(excludedProductsParameter);
		final List<String> excludedProducts = new ArrayList<>();
		excludedProducts.add("excludedProduct1");
		excludedProducts.add("excludedProduct2");
		when(excludedProductsParameter.getValue()).thenReturn(excludedProducts);

		final RuleIrCondition ruleIrCondition = translator.translate(context, condition, conditionDefinition);
		assertThat(ruleIrCondition, instanceOf(RuleIrGroupCondition.class));
		final RuleIrGroupCondition irGroupCondition = (RuleIrGroupCondition) ruleIrCondition;
		assertEquals(4, irGroupCondition.getChildren().size());
		checkChildConditions(irGroupCondition.getChildren(), RuleIrGroupOperator.OR);
		checkExcludeProductsCondition(irGroupCondition.getChildren().get(3));
	}

	private void checkExcludeProductsCondition(final RuleIrCondition ruleIrCondition)
	{
		assertThat(ruleIrCondition, instanceOf(RuleIrGroupCondition.class));
		final RuleIrGroupCondition excludeGroupCondition = (RuleIrGroupCondition) ruleIrCondition;
		assertEquals(3, excludeGroupCondition.getChildren().size());

		assertThat(excludeGroupCondition.getChildren().get(0), instanceOf(RuleIrAttributeCondition.class));
		final RuleIrAttributeCondition ruleIrAttributeProduct1Condition =
				(RuleIrAttributeCondition) excludeGroupCondition.getChildren().get(0);
		assertEquals(RuleIrAttributeOperator.NOT_IN, ruleIrAttributeProduct1Condition.getOperator());
		assertEquals(ORDER_ENTRY_RAO_PRODUCT_CODE_ATTRIBUTE, ruleIrAttributeProduct1Condition.getAttribute());
		assertEquals(List.of("excludedProduct1", "excludedProduct2"), ruleIrAttributeProduct1Condition.getValue());

		assertThat(excludeGroupCondition.getChildren().get(1), instanceOf(RuleIrAttributeCondition.class));
		final RuleIrAttributeCondition ruleIrAttributeProduct2Condition =
				(RuleIrAttributeCondition) excludeGroupCondition.getChildren().get(1);
		assertEquals(RuleIrAttributeOperator.NOT_CONTAINS, ruleIrAttributeProduct2Condition.getOperator());
		assertEquals(BASE_PRODUCT_CODES_ATTRIBUTE, ruleIrAttributeProduct2Condition.getAttribute());
		assertEquals("excludedProduct1", ruleIrAttributeProduct2Condition.getValue());

		assertThat(excludeGroupCondition.getChildren().get(2), instanceOf(RuleIrAttributeCondition.class));
		final RuleIrAttributeCondition ruleIrAttributeProduct3Condition =
				(RuleIrAttributeCondition) excludeGroupCondition.getChildren().get(2);
		assertEquals(RuleIrAttributeOperator.NOT_CONTAINS, ruleIrAttributeProduct3Condition.getOperator());
		assertEquals(BASE_PRODUCT_CODES_ATTRIBUTE, ruleIrAttributeProduct3Condition.getAttribute());
		assertEquals("excludedProduct2", ruleIrAttributeProduct3Condition.getValue());
	}


	@Test
	public void testTranslateAllOperatorCondition()
	{
		when(categoriesOperatorParameter.getValue()).thenReturn(CollectionOperator.CONTAINS_ALL);

		final RuleIrCondition ruleIrCondition = translator.translate(context, condition, conditionDefinition);
		assertThat(ruleIrCondition, instanceOf(RuleIrGroupCondition.class));
		final RuleIrGroupCondition irGroupCondition = (RuleIrGroupCondition) ruleIrCondition;
		assertEquals(4, irGroupCondition.getChildren().size());
		checkChildConditions(irGroupCondition.getChildren(), RuleIrGroupOperator.OR);
		assertThat(irGroupCondition.getChildren().get(3), instanceOf(RuleIrExistsCondition.class));
	}

	private void checkChildConditions(final List<RuleIrCondition> ruleIrConditions, final RuleIrGroupOperator groupOperator)
	{
		assertThat(ruleIrConditions.get(0), instanceOf(RuleIrGroupCondition.class));
		final RuleIrGroupCondition ruleIrGroupCondition = (RuleIrGroupCondition) ruleIrConditions.get(0);
		assertEquals(groupOperator, ruleIrGroupCondition.getOperator());

		assertEquals(2, ruleIrGroupCondition.getChildren().size());
		assertThat(ruleIrGroupCondition.getChildren().get(0), instanceOf(RuleIrAttributeCondition.class));
		final RuleIrAttributeCondition ruleIrAttributeCategory1Condition =
				(RuleIrAttributeCondition) ruleIrGroupCondition.getChildren().get(0);
		assertEquals(RuleIrAttributeOperator.CONTAINS, ruleIrAttributeCategory1Condition.getOperator());
		assertEquals(ORDER_ENTRY_RAO_CATEGORY_CODES_ATTRIBUTE, ruleIrAttributeCategory1Condition.getAttribute());
		assertEquals("categoryCode1", ruleIrAttributeCategory1Condition.getValue());

		assertThat(ruleIrGroupCondition.getChildren().get(1), instanceOf(RuleIrAttributeCondition.class));
		final RuleIrAttributeCondition ruleIrAttributeCategory2Condition =
				(RuleIrAttributeCondition) ruleIrGroupCondition.getChildren().get(1);
		assertEquals(RuleIrAttributeOperator.CONTAINS, ruleIrAttributeCategory2Condition.getOperator());
		assertEquals(ORDER_ENTRY_RAO_CATEGORY_CODES_ATTRIBUTE, ruleIrAttributeCategory2Condition.getAttribute());
		assertEquals("categoryCode2", ruleIrAttributeCategory2Condition.getValue());

		assertThat(ruleIrConditions.get(1), instanceOf(RuleIrAttributeCondition.class));
		final RuleIrAttributeCondition ruleIrAttributeOrderEntryQuantityCondition = (RuleIrAttributeCondition) ruleIrConditions
				.get(1);
		assertEquals(RuleIrAttributeOperator.GREATER_THAN, ruleIrAttributeOrderEntryQuantityCondition.getOperator());
		assertEquals(1, ruleIrAttributeOrderEntryQuantityCondition.getValue());

		assertThat(ruleIrConditions.get(2), instanceOf(RuleIrAttributeRelCondition.class));
		final RuleIrAttributeRelCondition RuleIrCartOrderEntryRelCondition = (RuleIrAttributeRelCondition) ruleIrConditions.get(2);
		assertEquals(RuleIrAttributeOperator.CONTAINS, RuleIrCartOrderEntryRelCondition.getOperator());
		assertEquals(CART_RAO_VAR, RuleIrCartOrderEntryRelCondition.getVariable());
		assertEquals(ORDER_ENTRY_RAO_VAR, RuleIrCartOrderEntryRelCondition.getTargetVariable());
		assertEquals(CART_RAO_ENTRIES_ATTRIBUTE, RuleIrCartOrderEntryRelCondition.getAttribute());

		verify(consumptionSupport).newProductConsumedCondition(context, ORDER_ENTRY_RAO_VAR);
	}
}
