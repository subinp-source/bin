/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.search;

import javax.validation.constraints.NotNull;

/**
 * Represents a where clause condition (e.g. "{attributeName} = 'attributeValue'").
 */
public class SimplePropertyWhereClauseCondition extends WhereClauseCondition
{
	private static final String ATTRIBUTE_VALUE_TEMPLATE = "'%s'";
	private static final String CONDITION_TEMPLATE = "{%s} %s %s";

	/**
	 * Stores a single where clause condition.
	 *
	 * @param attributeName - attribute name
	 * @param compareOperator - compare operator (e.g. "=")
	 * @param attributeValue - attribute value
	 */
	public SimplePropertyWhereClauseCondition(final String attributeName, final String compareOperator, final String attributeValue)
	{
		super(CONDITION_TEMPLATE, attributeName, compareOperator, String.format(ATTRIBUTE_VALUE_TEMPLATE, attributeValue));
	}

	/**
	 * Creates "equals" condition for performing a search when the specified attribute is equal to the provided value.
	 * @param attribute attribute the search should be performed by.
	 * @param value a value the attribute should be equal to.
	 * @return a where clause condition to be used for the search by the specified parameter
	 */
	public static @NotNull WhereClauseCondition eq(final String attribute, final Object value)
	{
		return new SimplePropertyWhereClauseCondition(attribute, "=", String.valueOf(value));
	}

	/**
	 * Creates condition with given operators for performing a search when the specified attribute is compared to the provided value.
	 * @param attribute attribute the search should be performed by.
	 * @param value a value the attribute should be compared to.
	 * @param operator a operator indicating how the attribute is compared to the value.
	 * @return a where clause condition to be used for the search by the specified parameter
	 */
	public static @NotNull WhereClauseCondition withCompareOperator(final String attribute, final Object value, final String operator)
	{
		return new SimplePropertyWhereClauseCondition(attribute, operator, String.valueOf(value));
	}
}
