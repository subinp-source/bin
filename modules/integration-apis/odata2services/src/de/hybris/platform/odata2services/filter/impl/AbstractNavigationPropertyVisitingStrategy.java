/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.filter.impl;

import static de.hybris.platform.odata2services.filter.impl.WhereClauseConditionUtil.EMPTY_CONDITIONS;

import de.hybris.platform.integrationservices.search.ItemSearchService;
import de.hybris.platform.integrationservices.search.WhereClauseConditions;
import de.hybris.platform.odata2services.filter.BinaryExpressionVisitingStrategy;
import de.hybris.platform.odata2services.odata.persistence.ItemLookupRequestFactory;
import de.hybris.platform.odata2services.odata.persistence.lookup.ItemLookupStrategy;

import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.api.uri.expression.BinaryExpression;
import org.apache.olingo.odata2.api.uri.expression.BinaryOperator;
import org.apache.olingo.odata2.api.uri.expression.MemberExpression;
import org.springframework.core.convert.converter.Converter;

/**
 * Provides common functionality to NavigationPropertyVisitingStrategies
 */
public abstract class AbstractNavigationPropertyVisitingStrategy implements BinaryExpressionVisitingStrategy
{
	private ItemLookupRequestFactory itemLookupRequestFactory;
	private ItemLookupStrategy itemLookupStrategy;
	private ItemSearchService itemSearchService;
	private ODataContext context;
	private Converter<BinaryOperator, String> operatorConverter;

	@Override
	public WhereClauseConditions visit(final BinaryExpression expression, final BinaryOperator operator, final Object leftResult,
	                                   final Object rightResult)
	{
		final String sqlOperator = getOperatorConverter().convert(operator);
		if (sqlOperator != null && sqlOperator.matches("=|<>|<|>|>=|<="))
		{
			return createWhereClauseConditionWithOperator(expression, operator, leftResult, rightResult);
		}
		return EMPTY_CONDITIONS;
	}

	protected abstract WhereClauseConditions createWhereClauseConditionWithOperator(final BinaryExpression expression,
	                                                                                final BinaryOperator operator,
	                                                                                final Object leftResult,
	                                                                                final Object rightResult);

	protected String getLeftOperandPropertyName(final BinaryExpression expression)
	{
		return ((MemberExpression) expression.getLeftOperand()).getProperty().getUriLiteral();
	}

	protected String getLeftOperandNavPropertyName(final BinaryExpression expression)
	{
		return ((MemberExpression) expression.getLeftOperand()).getPath().getUriLiteral();
	}

	protected ItemLookupRequestFactory getItemLookupRequestFactory()
	{
		return itemLookupRequestFactory;
	}

	public void setItemLookupRequestFactory(final ItemLookupRequestFactory itemLookupRequestFactory)
	{
		this.itemLookupRequestFactory = itemLookupRequestFactory;
	}

	 /**
	 * @deprecated use {@link #getItemSearchService()} instead.
	 */
	@Deprecated(since = "1905.2002-CEP", forRemoval = true)
	protected ItemLookupStrategy getItemLookupStrategy()
	{
		return itemLookupStrategy;
	}

	/**
	 * @deprecated use {@link #setItemSearchService(ItemSearchService)} instead
	 */
	@Deprecated(since = "1905.2002-CEP", forRemoval = true)
	public void setItemLookupStrategy(final ItemLookupStrategy itemLookupStrategy)
	{
		this.itemLookupStrategy = itemLookupStrategy;
	}

	ItemSearchService getItemSearchService()
	{
		return itemSearchService;
	}

	public void setItemSearchService(final ItemSearchService service)
	{
		itemSearchService = service;
	}

	protected ODataContext getContext()
	{
		return context;
	}

	public void setContext(final ODataContext context)
	{
		this.context = context;
	}

	protected Converter<BinaryOperator, String> getOperatorConverter()
	{
		return operatorConverter;
	}

	public void setOperatorConverter(final Converter<BinaryOperator, String> operatorConverter)
	{
		this.operatorConverter = operatorConverter;
	}
}
