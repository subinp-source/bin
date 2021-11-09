/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.AsException;
import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.data.AsExpressionData;
import de.hybris.platform.adaptivesearch.data.AsFacetSortData;
import de.hybris.platform.adaptivesearch.data.AsIndexConfigurationData;
import de.hybris.platform.adaptivesearch.data.AsIndexPropertyData;
import de.hybris.platform.adaptivesearch.data.AsIndexTypeData;
import de.hybris.platform.adaptivesearch.data.AsSearchQueryData;
import de.hybris.platform.adaptivesearch.data.AsSearchResultData;
import de.hybris.platform.adaptivesearch.enums.AsBoostOperator;
import de.hybris.platform.adaptivesearch.enums.AsBoostType;
import de.hybris.platform.adaptivesearch.enums.AsFacetType;
import de.hybris.platform.adaptivesearch.strategies.AsFeatureFlag;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProvider;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;


/**
 * Mock implementation for {@link AsSearchProvider}
 */
public class MockAsSearchProvider implements AsSearchProvider, InitializingBean
{
	protected static final String INDEX_CONFIGURATION = "testConfiguration";

	protected static final String INDEX_TYPE = "testIndex";
	protected static final String INDEX_TYPE1 = "testIndex1";
	protected static final String INDEX_TYPE2 = "testIndex2";
	protected static final String INDEX_TYPE_NO_CATALOG_VERSION = "indexNoCatalogVersion";

	protected static final String SCORE_EXPRESSION = "score";
	protected static final String INDEX_PROPERTY_CODE1 = "property1";
	protected static final String INDEX_PROPERTY_CODE2 = "property2";
	protected static final String INDEX_PROPERTY_CODE3 = "property3";
	protected static final String INDEX_PROPERTY_CODE4 = "property4";

	private final Map<String, AsIndexConfigurationData> indexConfigurations = new HashMap<>();
	private final Map<String, AsIndexTypeData> indexTypes = new HashMap<>();
	private final Map<String, AsIndexPropertyData> indexProperties = new HashMap<>();

	@Override
	public void afterPropertiesSet()
	{
		// index configurations

		final AsIndexConfigurationData indexConfiguration = new AsIndexConfigurationData();
		indexConfiguration.setCode(INDEX_CONFIGURATION);
		indexConfiguration.setName(INDEX_CONFIGURATION);

		indexConfigurations.put(indexConfiguration.getCode(), indexConfiguration);

		// index types

		final AsIndexTypeData indexType = new AsIndexTypeData();
		indexType.setCode(INDEX_TYPE);
		indexType.setName(INDEX_TYPE);

		final AsIndexTypeData indexType1 = new AsIndexTypeData();
		indexType1.setCode(INDEX_TYPE1);
		indexType1.setName(INDEX_TYPE1);

		final AsIndexTypeData indexType2 = new AsIndexTypeData();
		indexType2.setCode(INDEX_TYPE2);
		indexType2.setName(INDEX_TYPE2);

		final AsIndexTypeData indexTypeNoCatalogVersion = new AsIndexTypeData();
		indexTypeNoCatalogVersion.setCode(INDEX_TYPE_NO_CATALOG_VERSION);
		indexTypeNoCatalogVersion.setName(INDEX_TYPE_NO_CATALOG_VERSION);

		indexTypes.put(indexType.getCode(), indexType);
		indexTypes.put(indexType1.getCode(), indexType1);
		indexTypes.put(indexType2.getCode(), indexType2);
		indexTypes.put(indexTypeNoCatalogVersion.getCode(), indexTypeNoCatalogVersion);

		// index properties

		final AsIndexPropertyData indexProperty1 = new AsIndexPropertyData();
		indexProperty1.setCode(INDEX_PROPERTY_CODE1);
		indexProperty1.setName(INDEX_PROPERTY_CODE1);
		indexProperty1.setType(String.class);
		indexProperty1.setSupportedBoostOperators(Collections.singleton(AsBoostOperator.EQUAL));

		final AsIndexPropertyData indexProperty2 = new AsIndexPropertyData();
		indexProperty2.setCode(INDEX_PROPERTY_CODE2);
		indexProperty2.setName(INDEX_PROPERTY_CODE2);
		indexProperty2.setType(String.class);
		indexProperty2.setSupportedBoostOperators(Collections.singleton(AsBoostOperator.EQUAL));

		final AsIndexPropertyData indexProperty3 = new AsIndexPropertyData();
		indexProperty3.setCode(INDEX_PROPERTY_CODE3);
		indexProperty3.setName(INDEX_PROPERTY_CODE3);
		indexProperty3.setType(Float.class);
		indexProperty3.setSupportedBoostOperators(Collections.singleton(AsBoostOperator.GREATER_THAN));

		final AsIndexPropertyData indexProperty4 = new AsIndexPropertyData();
		indexProperty4.setCode(INDEX_PROPERTY_CODE4);
		indexProperty4.setName(INDEX_PROPERTY_CODE4);
		indexProperty4.setType(String.class);
		indexProperty4.setSupportedBoostOperators(Collections.singleton(AsBoostOperator.EQUAL));

		indexProperties.put(indexProperty1.getCode(), indexProperty1);
		indexProperties.put(indexProperty2.getCode(), indexProperty2);
		indexProperties.put(indexProperty3.getCode(), indexProperty3);
		indexProperties.put(indexProperty4.getCode(), indexProperty4);
	}

	@Override
	public Set<AsFeatureFlag> getSupportedFeatures(final String indexType)
	{
		return EnumSet.allOf(AsFeatureFlag.class);
	}

	@Override
	public List<AsIndexConfigurationData> getIndexConfigurations()
	{
		return new ArrayList<>(indexConfigurations.values());
	}

	@Override
	public Optional<AsIndexConfigurationData> getIndexConfigurationForCode(final String code)
	{
		return Optional.ofNullable(indexConfigurations.get(code));
	}

	@Override
	public List<AsIndexTypeData> getIndexTypes()
	{
		return new ArrayList<>(indexTypes.values());
	}

	@Override
	public List<AsIndexTypeData> getIndexTypes(final String indexConfiguration)
	{
		return new ArrayList<>(indexTypes.values());
	}

	@Override
	public Optional<AsIndexTypeData> getIndexTypeForCode(final String code)
	{
		return Optional.ofNullable(indexTypes.get(code));
	}

	@Override
	public List<AsIndexPropertyData> getIndexProperties(final String indexType)
	{
		return new ArrayList<>(indexProperties.values());
	}

	@Override
	public Optional<AsIndexPropertyData> getIndexPropertyForCode(final String indexType, final String code)
	{
		return Optional.ofNullable(indexProperties.get(code));
	}

	@Override
	public List<CatalogVersionModel> getSupportedCatalogVersions(final String indexConfiguration, final String indexType)
	{
		return Collections.emptyList();
	}

	@Override
	public List<LanguageModel> getSupportedLanguages(final String indexConfiguration, final String indexType)
	{
		return Collections.emptyList();
	}

	@Override
	public List<CurrencyModel> getSupportedCurrencies(final String indexConfiguration, final String indexType)
	{
		return Collections.emptyList();
	}

	@Override
	public List<AsIndexPropertyData> getSupportedFacetIndexProperties(final String indexType)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isValidFacetIndexProperty(final String indexType, final String code)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public List<AsExpressionData> getSupportedFacetExpressions(final String indexType)
	{
		return indexProperties.values().stream().filter(indexProperty -> isValidFacetExpression(indexType, indexProperty.getCode()))
				.map(this::createExpression).collect(Collectors.toList());
	}

	@Override
	public boolean isValidFacetExpression(final String indexType, final String expression)
	{
		return StringUtils.equals(expression, INDEX_PROPERTY_CODE1) || StringUtils.equals(expression, INDEX_PROPERTY_CODE2)
				|| StringUtils.equals(expression, INDEX_PROPERTY_CODE3);
	}

	@Override
	public List<AsFacetType> getSupportedFacetTypes(final String indexType)
	{
		return Arrays.asList(AsFacetType.values());
	}

	@Override
	public boolean isValidFacetType(final String indexType, final AsFacetType facetType)
	{
		return true;
	}

	@Override
	public List<AsFacetSortData> getSupportedFacetSorts(final String indexType)
	{
		return Collections.emptyList();
	}

	@Override
	public boolean isValidFacetSort(final String indexType, final String sort)
	{
		return false;
	}

	@Override
	public List<AsBoostType> getSupportedBoostTypes(final String indexType)
	{
		return Arrays.asList(AsBoostType.values());
	}

	@Override
	public boolean isValidBoostType(final String indexType, final AsBoostType boostType)
	{
		return true;
	}

	@Override
	public List<AsExpressionData> getSupportedSortExpressions(final String indexType)
	{
		final List<AsExpressionData> expressions = indexProperties.values().stream()
				.filter(indexProperty -> isValidSortExpression(indexType, indexProperty.getCode())).map(this::createExpression)
				.collect(Collectors.toList());

		final AsExpressionData scoreExpression = new AsExpressionData();
		scoreExpression.setExpression(SCORE_EXPRESSION);

		expressions.add(0, scoreExpression);

		return expressions;
	}

	@Override
	public boolean isValidSortExpression(final String indexType, final String expression)
	{
		return StringUtils.equals(expression, SCORE_EXPRESSION) || StringUtils.equals(expression, INDEX_PROPERTY_CODE1)
				|| StringUtils.equals(expression, INDEX_PROPERTY_CODE2) || StringUtils.equals(expression, INDEX_PROPERTY_CODE3);
	}

	@Override
	public List<AsExpressionData> getSupportedGroupExpressions(final String indexType)
	{
		return indexProperties.values().stream().filter(indexProperty -> isValidGroupExpression(indexType, indexProperty.getCode()))
				.map(indexProperty -> {
					final AsExpressionData expression = new AsExpressionData();
					expression.setExpression(indexProperty.getCode());
					expression.setName(indexProperty.getName());

					return expression;
				}).collect(Collectors.toList());
	}

	@Override
	public boolean isValidGroupExpression(final String indexType, final String expression)
	{
		return StringUtils.equals(expression, INDEX_PROPERTY_CODE1) || StringUtils.equals(expression, INDEX_PROPERTY_CODE2)
				|| StringUtils.equals(expression, INDEX_PROPERTY_CODE3);
	}

	@Override
	public AsSearchResultData search(final AsSearchProfileContext context, final AsSearchQueryData searchQuery) throws AsException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public List<String> getAvailableQualifiers(final String indexType, final String indexPropertyCode) throws AsException
	{
		return Collections.emptyList();
	}

	protected AsExpressionData createExpression(final AsIndexPropertyData indexProperty)
	{
		final AsExpressionData expression = new AsExpressionData();
		expression.setExpression(indexProperty.getCode());
		expression.setName(indexProperty.getName());

		return expression;
	}
}
