/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.predicate;

import java.util.function.Predicate;
import java.util.regex.Pattern;


/**
 * Predicate to test if a given string is a supported one.
 * <p>
 * Returns <tt>TRUE</tt> if the input is composed of alphanumeric or hyphen characters;  <tt>FALSE</tt> otherwise.
 * </p>
 */
public class OnlyHasSupportedCharactersPredicate implements Predicate<String>
{

	@Override
	public boolean test(final String target)
	{
		final Pattern regex = Pattern.compile("^[a-zA-Z0-9-_]+$");
		return regex.matcher(target).find();
	}

}
