/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.search;

/**
 * Represents a where clause condition (e.g. "{attributeName} = attributeValue").
 */
public class NavigationPropertyWithIntegrationKeyWhereClauseCondition extends WhereClauseCondition
{
	private static final String CONDITION_TEMPLATE = "{%s} %s %s";
	private static final String COMPARE_OPERATOR = "=";

	/**
	 * Stores a single where clause condition.
	 *
	 * @param attributeName - attribute name
	 * @param attributeValue - attribute value
	 */
	public NavigationPropertyWithIntegrationKeyWhereClauseCondition(final String attributeName, final String attributeValue)
	{
		super(CONDITION_TEMPLATE, attributeName, COMPARE_OPERATOR, attributeValue);
	}
}
