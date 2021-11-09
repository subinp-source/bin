/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.util;

import de.hybris.platform.b2b.enums.B2BPeriodRange;
import de.hybris.platform.util.StandardDateRange;


public interface DateRangeFactory
{
	public <T extends StandardDateRange> T createDateRange(final B2BPeriodRange range);
}
