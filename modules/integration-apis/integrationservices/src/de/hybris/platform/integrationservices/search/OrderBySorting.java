/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.search;

/**
 * Enumeration describing all possible sort orders used in flexible search
 *
 */
public enum OrderBySorting
{

	ASC("ASC"),
	DESC("DESC");

	private final String sortOrder;

	OrderBySorting(final String sortOrder)
	{
		this.sortOrder = sortOrder;
	}

	@Override
	public String toString()
	{
		return sortOrder;
	}

}
