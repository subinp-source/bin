/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.search;

/**
 * Represents a where clause condition where we have no result.
 * Using this class allows us to use OR conjunctive operators where we may
 * have some where clause conditions that do not have results.
 */
public class NoResultWhereClauseCondition extends WhereClauseCondition
{
	private static final String NO_RESULT_TEMPLATE = "NO_RESULT%s%s%s";

	/**
	 * Constructor to create a NoResultWhereClauseCondition
	 */
	public NoResultWhereClauseCondition()
	{
		super(NO_RESULT_TEMPLATE, "", "", "");
	}
}
