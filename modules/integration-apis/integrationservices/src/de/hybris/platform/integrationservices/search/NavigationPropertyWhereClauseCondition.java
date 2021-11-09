/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.search;

/**
 * Represents a where clause condition (e.g. "{attributeName} IN (attributeValue)").
 */
public class NavigationPropertyWhereClauseCondition extends WhereClauseCondition
{
	private static final String ATTRIBUTE_VALUE_TEMPLATE = "(%s)";
	private static final String CONDITION_TEMPLATE = "{%s} %s %s";
	private static final String COMPARE_OPERATOR = "IN";

	/**
	 * Stores a single where clause condition.
	 *
	 * @param attributeName - attribute name
	 * @param attributeValue - attribute value
	 */
	public NavigationPropertyWhereClauseCondition(final String attributeName, final String attributeValue)
	{
		super(CONDITION_TEMPLATE, attributeName, COMPARE_OPERATOR, String.format(ATTRIBUTE_VALUE_TEMPLATE, attributeValue));
	}
}
