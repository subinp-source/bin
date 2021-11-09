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
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.services.RuleConditionsRegistry;
import de.hybris.platform.ruleengineservices.rule.services.RuleConditionsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class SourceRuleConditionOverlapValidatorTest
{
	@Mock
	private RuleConditionsService ruleConditionsService;
	@Mock
	private RuleConditionsRegistry ruleConditionsRegistry;
	@Mock
	private Function<RuleConditionData, List<CategoryModel>> conditionToCategoriesMapper;
	@Mock
	private Function<RuleConditionData, List<ProductModel>> conditionToProductsMapper;
	@Mock
	private Function<ProductModel, Set<String>> productToCodesMapper;
	@Mock
	private Function<CategoryModel, Set<String>> categoryToCodesMapper;
	@Mock
	private SourceRuleModel sourceRuleModel;

	@InjectMocks
	private SourceRuleConditionOverlapValidator validator;

	@Before
	public void setUp()
	{
		validator.setContainerCountThreshold(3);
		validator.setOverlapCountThreshold(3);
		validator.setContainerDefinitionId("containerDefinitionId");

		when(sourceRuleModel.getConditions()).thenReturn("conditions");
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIEAOnNullParameter()
	{
		validator.test(null);
	}

	@Test
	public void shouldReturnTrueOnContainersSizeLessThanThreshold()
	{
		final Map<String, RuleConditionDefinitionData> conditionDefinitions = mock(Map.class);
		when(ruleConditionsRegistry.getConditionDefinitionsForRuleTypeAsMap(sourceRuleModel.getClass()))
				.thenReturn(conditionDefinitions);

		final RuleConditionData ruleCondition1 = mock(RuleConditionData.class);
		final RuleConditionData ruleCondition2 = mock(RuleConditionData.class);
		final RuleConditionData ruleCondition3 = mock(RuleConditionData.class);

		final List<RuleConditionData> ruleConditions = List.of(ruleCondition1, ruleCondition2, ruleCondition3);
		when(ruleConditionsService.convertConditionsFromString("conditions", conditionDefinitions)).thenReturn(ruleConditions);

		final boolean result = validator.test(sourceRuleModel);

		assertThat(result).isTrue();
	}

	@Test
	public void shouldReturnFalseOnContainersOverlap()
	{
		final Map<String, RuleConditionDefinitionData> conditionDefinitions = mock(Map.class);
		when(ruleConditionsRegistry.getConditionDefinitionsForRuleTypeAsMap(sourceRuleModel.getClass()))
				.thenReturn(conditionDefinitions);

		final RuleConditionData ruleCondition1 = mock(RuleConditionData.class);
		when(ruleCondition1.getDefinitionId()).thenReturn("containerDefinitionId");

		final RuleConditionData ruleCondition2 = mock(RuleConditionData.class);

		final RuleConditionData ruleCondition3 = mock(RuleConditionData.class);
		when(ruleCondition3.getDefinitionId()).thenReturn("containerDefinitionId");

		final RuleConditionData ruleCondition4 = mock(RuleConditionData.class);

		final RuleConditionData ruleCondition5 = mock(RuleConditionData.class);
		when(ruleCondition5.getDefinitionId()).thenReturn("containerDefinitionId");

		final List<RuleConditionData> ruleConditions = List.of(ruleCondition1, ruleCondition2, ruleCondition3, ruleCondition4,
				ruleCondition5);

		final CategoryModel category11 = category("category11");
		final CategoryModel category12 = category("category12", category11);
		final CategoryModel category31 = category("category31", category11);
		final CategoryModel category51 = category("category51", category31, category11);
		final CategoryModel category52 = category("category52", category12, category11);
		final CategoryModel category53 = category("category53", category11);
		when(conditionToCategoriesMapper.apply(ruleCondition1)).thenReturn(List.of(category11, category12));
		when(conditionToCategoriesMapper.apply(ruleCondition3)).thenReturn(List.of(category31));
		when(conditionToCategoriesMapper.apply(ruleCondition5)).thenReturn(List.of(category51, category52, category53));

		final ProductModel product11 = product("product11", category11, category12);
		final ProductModel product51 = product("product51", category31, category51, category52);
		final ProductModel product52 = product("product52", category52, category53);
		when(conditionToProductsMapper.apply(ruleCondition1)).thenReturn(List.of(product11));
		when(conditionToProductsMapper.apply(ruleCondition3)).thenReturn(List.of());
		when(conditionToProductsMapper.apply(ruleCondition5)).thenReturn(List.of(product51, product52));

		when(ruleConditionsService.convertConditionsFromString("conditions", conditionDefinitions)).thenReturn(ruleConditions);

		final boolean result = validator.test(sourceRuleModel);

		assertThat(result).isFalse();
	}

	protected CategoryModel category(final String code, final CategoryModel... relatedCategories)
	{
		final CategoryModel category = mock(CategoryModel.class);
		when(category.getCode()).thenReturn(code);
		final Set<String> codes = Arrays.stream(relatedCategories).map(CategoryModel::getCode).collect(Collectors.toSet());
		codes.add(code);
		when(categoryToCodesMapper.apply(category)).thenReturn(codes);
		return category;
	}

	protected ProductModel product(final String code, final CategoryModel... superCategories)
	{
		final ProductModel product = mock(ProductModel.class);
		when(product.getCode()).thenReturn(code);
		final Set<String> categoryCodes = Arrays.stream(superCategories).map(CategoryModel::getCode).collect(Collectors.toSet());
		categoryCodes.add(code);
		when(productToCodesMapper.apply(product)).thenReturn(categoryCodes);
		return product;
	}
}
