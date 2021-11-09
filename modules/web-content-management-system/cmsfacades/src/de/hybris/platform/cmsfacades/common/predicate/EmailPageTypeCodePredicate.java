/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.predicate;

import java.util.function.Predicate;


/**
 * Predicate to test if a given page type code is a EmailPage code.
 * <p>
 * Note that the email page type is defined in acceleratorservices and having a reference to the model in this extension
 * creates a circular dependency.
 *
 * @deprecated since 2005, no longer needed.
 */
@Deprecated(since = "2005", forRemoval = true)
public class EmailPageTypeCodePredicate implements Predicate<String>
{
	@Override
	public boolean test(final String pageTypeCode)
	{
		return "EmailPage".equals(pageTypeCode);
	}
}
