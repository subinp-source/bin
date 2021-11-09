/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.search;

import static de.hybris.platform.integrationservices.search.ClauseBuilderUtil.getItemAlias;
import static de.hybris.platform.integrationservices.search.ClauseBuilderUtil.getRelationAlias;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;

import org.apache.commons.lang.StringUtils;

/**
 * Represents a where clause condition (e.g. "{itemTypeAlias:attributeName} = attributeValue").
 */
class ItemWhereClauseCondition extends WhereClauseCondition
{
	private static final String PREFIX = "{%s:";
	private static final String SUFFIX = "%s} %s %s";
	private static final String COMPARE_OPERATOR = "=";

	/**
	 * Stores the where clause condition for an item's attribute with a conjunctive operator.
	 *
	 * @param attributeDescriptor - attribute descriptor model
	 * @param attributeName - attribute name
	 * @param filter - where clause condition
	 */
	ItemWhereClauseCondition(final AttributeDescriptorModel attributeDescriptor, final String attributeName, final WhereClauseCondition filter)
	{
		super(String.format(PREFIX, getRelationAlias(attributeDescriptor)) + SUFFIX,
				attributeName,
				deriveCompareOperator(filter),
				filter.getAttributeValue(),
				filter.getConjunctiveOperator());
	}

	/**
	 * Stores the where clause condition for an item's attribute with a conjunctive operator.
	 *
	 * @param itemModel - item model
	 * @param filter - where clause condition
	 */
	ItemWhereClauseCondition(final IntegrationObjectItemModel itemModel, final WhereClauseCondition filter)
	{
		super(String.format(PREFIX, getItemAlias(itemModel)) + SUFFIX,
				filter.getAttributeName(),
				deriveCompareOperator(filter),
				filter.getAttributeValue(),
				filter.getConjunctiveOperator());
	}

	/**
	 * Filter's attributeName is a type's code.
	 * Used in a JOIN clause.
	 *
	 * @param filter - where clause condition
	 */
	ItemWhereClauseCondition(final WhereClauseCondition filter)
	{
		super(String.format(PREFIX, filter.getAttributeName()) + SUFFIX,
				"pk",
				deriveCompareOperator(filter),
				filter.getAttributeValue(),
				filter.getConjunctiveOperator());
	}

	private static String deriveCompareOperator(final WhereClauseCondition filter)
	{
		return StringUtils.isEmpty(filter.getCompareOperator()) ? COMPARE_OPERATOR : filter.getCompareOperator().strip();
	}
}
