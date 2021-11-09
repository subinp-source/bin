/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package de.hybris.platform.integrationbackoffice.utility;

import java.util.regex.Pattern;

import org.apache.commons.lang3.RegExUtils;


/**
 * Provides utility methods for qualifier naming
 */
public class QualifierNameUtils
{
	private static final String ONLY_ALPHA_NUMERIC_REGEX = "^\\w+$";
	private static final String NON_ALPHA_NUMERIC_CHARACTER_REGEX = "\\W";
	private static final Pattern ONLY_ALPHA_NUMERIC_PATTERN = Pattern.compile(ONLY_ALPHA_NUMERIC_REGEX);

	private QualifierNameUtils()
	{
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Checks if given name contains only alpha-numeric characters
	 * 
	 * @param name name that will be checked for non-alpha-numeric characters
	 * @return true if the name consists only of alpha-numeric characters, false otherwise.
	 */
	public static boolean isAlphaNumericName(final String name)
	{
		return ONLY_ALPHA_NUMERIC_PATTERN.matcher(name).matches();
	}

	/**
	 * Removes all non-alpha-numeric characters from given text and returns it
	 * 
	 * @param name name that should be cleared off alpha-numeric characters
	 * @return name that consists of only alpha-numeric characters.
	 */
	public static String removeNonAlphaNumericCharacters(final String name)
	{
		return RegExUtils.removePattern(name, NON_ALPHA_NUMERIC_CHARACTER_REGEX);
	}
}
