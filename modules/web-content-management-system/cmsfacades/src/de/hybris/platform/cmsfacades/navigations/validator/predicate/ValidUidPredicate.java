/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.navigations.validator.predicate;


import de.hybris.platform.cms2.constants.Cms2Constants;

import java.util.function.Predicate;

import org.apache.commons.lang.StringUtils;


/**
 * Validates if the Navigation Node UID is valid
 * @deprecated since 1811 - no longer needed
 */
@Deprecated(since = "1811", forRemoval = true)
public class ValidUidPredicate implements Predicate<String>
{
	@Override
	public boolean test(final String target)
	{
		if (StringUtils.endsWithIgnoreCase(Cms2Constants.ROOT, target))
		{
			return false;
		}
		return true;
	}
}
