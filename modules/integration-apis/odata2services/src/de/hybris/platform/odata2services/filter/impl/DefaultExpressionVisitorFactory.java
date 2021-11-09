/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.filter.impl;

import de.hybris.platform.integrationservices.search.ItemSearchService;
import de.hybris.platform.odata2services.filter.BinaryExpressionVisitingStrategy;
import de.hybris.platform.odata2services.filter.BinaryExpressionVisitor;
import de.hybris.platform.odata2services.filter.ExpressionVisitorFactory;
import de.hybris.platform.odata2services.filter.ExpressionVisitorParameters;
import de.hybris.platform.odata2services.filter.FilterExpressionVisitor;
import de.hybris.platform.odata2services.filter.LiteralExpressionVisitor;
import de.hybris.platform.odata2services.filter.MemberExpressionVisitor;
import de.hybris.platform.odata2services.filter.OrderByExpressionVisitor;
import de.hybris.platform.odata2services.filter.OrderExpressionVisitor;
import de.hybris.platform.odata2services.filter.PropertyExpressionVisitor;
import de.hybris.platform.odata2services.odata.integrationkey.IntegrationKeyToODataEntryGenerator;
import de.hybris.platform.odata2services.odata.persistence.ItemLookupRequestFactory;
import de.hybris.platform.odata2services.odata.persistence.lookup.ItemLookupStrategy;
import de.hybris.platform.odata2services.odata.schema.entity.EntitySetNameGenerator;

import org.apache.olingo.odata2.api.uri.expression.BinaryOperator;
import org.apache.olingo.odata2.api.uri.expression.ExpressionVisitor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.convert.converter.Converter;

import com.google.common.collect.Lists;

/**
 * The default implementation of the {@link ExpressionVisitorFactory}
 */
public class DefaultExpressionVisitorFactory implements ExpressionVisitorFactory
{
	private IntegrationKeyToODataEntryGenerator integrationKeyConverter;
	private ItemLookupRequestFactory itemLookupRequestFactory;
	private ItemLookupStrategy itemLookupStrategy;
	private ItemSearchService itemSearchService;
	private Converter<BinaryOperator, String> operatorConverter;
	private EntitySetNameGenerator entitySetNameGenerator;

	@Override
	public ExpressionVisitor create(final ExpressionVisitorParameters parameters)
	{
		final DefaultExpressionVisitor visitor = new DefaultExpressionVisitor();
		visitor.setFilterExpressionVisitor(createFilterExpressionVisitor());
		visitor.setBinaryExpressionVisitor(createBinaryExpressionVisitor(parameters));
		visitor.setMemberExpressionVisitor(createMemberExpressionVisitor(parameters));
		visitor.setPropertyExpressionVisitor(createPropertyExpressionVisitor());
		visitor.setLiteralExpressionVisitor(createLiteralExpressionVisitor());
		visitor.setOrderByExpressionVisitor(createOrderByExpressionVisitor());
		visitor.setOrderExpressionVisitor(createOrderExpressionVisitor());

		return visitor;
	}

	protected FilterExpressionVisitor createFilterExpressionVisitor()
	{
		return new DefaultFilterExpressionVisitor();
	}

	protected OrderByExpressionVisitor createOrderByExpressionVisitor()
	{
		final DefaultOrderByExpressionVisitor visitor = new DefaultOrderByExpressionVisitor();
		return visitor;
	}

	protected OrderExpressionVisitor createOrderExpressionVisitor()
	{
		return new DefaultOrderExpressionVisitor();
	}

	protected BinaryExpressionVisitor createBinaryExpressionVisitor(final ExpressionVisitorParameters parameters)
	{
		final DefaultBinaryExpressionVisitor visitor = new DefaultBinaryExpressionVisitor();
		visitor.setStrategies(Lists.newArrayList(
				createSimplePropertyVisitingStrategy(),
				createNavigationPropertyVisitingStrategy(parameters),
				createNavigationPropertyWithIntegrationKeyVisitingStrategy(parameters),
				createCombineWhereClauseConditionsVisitingStrategy()
		));
		return visitor;
	}

	protected MemberExpressionVisitor createMemberExpressionVisitor(final ExpressionVisitorParameters parameters)
	{
		final DefaultMemberExpressionVisitor visitor = new DefaultMemberExpressionVisitor();
		visitor.setEntitySetNameGenerator(getEntitySetNameGenerator());
		visitor.setUriInfo(parameters.getUriInfo());
		return visitor;
	}

	protected PropertyExpressionVisitor createPropertyExpressionVisitor()
	{
		return new DefaultPropertyExpressionVisitor();
	}

	protected LiteralExpressionVisitor createLiteralExpressionVisitor()
	{
		return new DefaultLiteralExpressionVisitor();
	}

	protected BinaryExpressionVisitingStrategy createSimplePropertyVisitingStrategy()
	{
		final SimplePropertyVisitingStrategy strategy = new SimplePropertyVisitingStrategy();
		strategy.setOperatorConverter(getOperatorConverter());
		return strategy;
	}

	protected BinaryExpressionVisitingStrategy createNavigationPropertyVisitingStrategy(
			final ExpressionVisitorParameters parameters)
	{
		return setCommonFields(new NavigationPropertyVisitingStrategy(), parameters);
	}

	protected BinaryExpressionVisitingStrategy createNavigationPropertyWithIntegrationKeyVisitingStrategy(
			final ExpressionVisitorParameters parameters)
	{
		final NavigationPropertyWithIntegrationKeyVisitingStrategy strategy = new NavigationPropertyWithIntegrationKeyVisitingStrategy();
		strategy.setIntegrationKeyConverter(getIntegrationKeyConverter());
		return setCommonFields(strategy, parameters);
	}

	private AbstractNavigationPropertyVisitingStrategy setCommonFields(final AbstractNavigationPropertyVisitingStrategy strategy,
	                                                                   final ExpressionVisitorParameters parameters)
	{
		strategy.setContext(parameters.getContext());
		strategy.setItemLookupRequestFactory(getItemLookupRequestFactory());
		strategy.setItemSearchService(itemSearchService);
		strategy.setOperatorConverter(getOperatorConverter());
		return strategy;
	}

	protected BinaryExpressionVisitingStrategy createCombineWhereClauseConditionsVisitingStrategy()
	{
		final CombineWhereClauseConditionVisitingStrategy strategy = new CombineWhereClauseConditionVisitingStrategy();
		strategy.setOperatorConverter(getOperatorConverter());
		return strategy;
	}

	protected IntegrationKeyToODataEntryGenerator getIntegrationKeyConverter()
	{
		return integrationKeyConverter;
	}

	@Required
	public void setIntegrationKeyConverter(final IntegrationKeyToODataEntryGenerator integrationKeyConverter)
	{
		this.integrationKeyConverter = integrationKeyConverter;
	}

	protected ItemLookupRequestFactory getItemLookupRequestFactory()
	{
		return itemLookupRequestFactory;
	}

	@Required
	public void setItemLookupRequestFactory(final ItemLookupRequestFactory itemLookupRequestFactory)
	{
		this.itemLookupRequestFactory = itemLookupRequestFactory;
	}

	/**
	 * @deprecated subclasses can override {@link #setItemSearchService(ItemSearchService)} method and capture that value to use
	 * instead of the deprecated {@link ItemLookupStrategy}. Better yet, customizations should be done by using composition and
	 * delegating to this factory (Proxy or Chain of Responsibilities patterns) instead of subclassing this implementation.
	 */
	@Deprecated(since = "1905.2002-CEP", forRemoval = true)
	protected ItemLookupStrategy getItemLookupStrategy()
	{
		return itemLookupStrategy;
	}

	/**
	 * @deprecated Use {@link #setItemSearchService(ItemSearchService)} instead.
	 */
	@Deprecated(since = "1905.2002-CEP", forRemoval = true)
	public void setItemLookupStrategy(final ItemLookupStrategy itemLookupStrategy)
	{
		this.itemLookupStrategy = itemLookupStrategy;
	}

	@Required
	public void setItemSearchService(final ItemSearchService service)
	{
		itemSearchService = service;
	}

	protected Converter<BinaryOperator, String> getOperatorConverter()
	{
		return operatorConverter;
	}

	@Required
	public void setOperatorConverter(final Converter<BinaryOperator, String> operatorConverter)
	{
		this.operatorConverter = operatorConverter;
	}

	protected EntitySetNameGenerator getEntitySetNameGenerator()
	{
		return entitySetNameGenerator;
	}

	@Required
	public void setEntitySetNameGenerator(final EntitySetNameGenerator entitySetNameGenerator)
	{
		this.entitySetNameGenerator = entitySetNameGenerator;
	}

}
