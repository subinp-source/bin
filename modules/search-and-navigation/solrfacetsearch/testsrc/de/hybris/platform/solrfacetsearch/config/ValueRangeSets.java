/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.solrfacetsearch.config;

import java.util.List;


public final class ValueRangeSets
{
	// Suppresses default constructor, ensuring non-instantiability.
	private ValueRangeSets()
	{
	}

	public static ValueRangeSet createValueRangeSet(final String qualifier, final List<ValueRange> valueRanges)
	{
		final ValueRangeSet set = new ValueRangeSet();
		set.setQualifier(qualifier);
		set.setValueRanges(valueRanges);
		return set;
	}
}
