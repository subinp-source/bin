/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.search;

import static de.hybris.platform.integrationservices.search.ClauseBuilderUtil.getItemAlias;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

/**
 * Build the order by clause for flexible search
 */
class OrderByClauseBuilder
{
	private List<OrderExpression> orderByExpressions;
	private String selectClause;
	private IntegrationObjectItemModel itemModel;
	private Locale locale;

	private OrderByClauseBuilder()
	{
		//Private Constructor
	}

	static OrderByClauseBuilder builder()
	{
		return new OrderByClauseBuilder();
	}

	OrderByClauseBuilder withOrderBy(final List<OrderExpression> orderByExpressions)
	{
		this.orderByExpressions = orderByExpressions;
		return this;
	}

	OrderByClauseBuilder withSelectClause(final String selectClause)
	{
		this.selectClause = selectClause;
		return this;
	}

	OrderByClauseBuilder withIntegrationObjectItem(final IntegrationObjectItemModel itemModel)
	{
		this.itemModel = itemModel;
		return this;
	}

	OrderByClauseBuilder withLocale(final Locale locale)
	{
		this.locale = locale;
		return this;
	}


	String buildSelectClause()
	{

		final StringBuilder selectClauseWithOrderBy = new StringBuilder(ItemModel.PK).append("},");

		orderByExpressions.forEach(expression ->
				selectClauseWithOrderBy.append(" {")
				                       .append(getItemAlias(itemModel))
				                       .append(":")
				                       .append(ClauseBuilderUtil.changeOrderByToUsePlatformAttributeName(expression,
						                       itemModel,
						                       locale))
				                       .append("},"));

		return selectClause.replace(ItemModel.PK + "}", StringUtils.removeEnd(selectClauseWithOrderBy.toString(), ","));
	}

	String buildOrderByClause()
	{
		validateOrderByClauseDoesNotContainVirtualAttribute();
		final StringBuilder orderByClause = new StringBuilder(" ORDER BY");
		orderByExpressions.forEach(expression ->
				orderByClause.append(" {")
				             .append(getItemAlias(itemModel))
				             .append(":")
				             .append(ClauseBuilderUtil.changeOrderByToUsePlatformAttributeName(expression,
						             itemModel,
						             locale))
				             .append("} ")
				             .append(expression.getOrderBySorting())
				             .append(",")
		);

		return StringUtils.removeEnd(orderByClause.toString(), ",");
	}

	private void validateOrderByClauseDoesNotContainVirtualAttribute()
	{
		orderByExpressions.stream()
		                  .map(OrderExpression::getOrderBy)
		                  .map(name -> ClauseBuilderUtil.getVirtualAttributeModel(itemModel, name))
		                  .filter(Optional::isPresent)
		                  .map(Optional::get)
		                  .findFirst()
		                  .ifPresent(attribute -> {
			                  throw new OrderByVirtualAttributeNotSupportedException(attribute);
		                  });
	}
}