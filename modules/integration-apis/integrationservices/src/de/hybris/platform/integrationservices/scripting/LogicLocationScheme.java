/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.scripting;

import java.util.stream.Stream;

/**
 * Type of schemes supported for the logic location
 */
public enum LogicLocationScheme
{
	MODEL;

	/**
	 * Returns the enum for the given scheme
	 * @param scheme String representation of the enumeration to retrieve
	 * @return The enumeration that is equivalent to the given scheme
	 * @throws UnsupportedLogicLocationSchemeException when the given scheme does not match any of the enumerations
	 */
	public static LogicLocationScheme from(final String scheme) throws UnsupportedLogicLocationSchemeException
	{
		return Stream.of(values())
					 .filter(s -> s.name().equalsIgnoreCase(scheme))
					 .findFirst()
					 .orElseThrow(() -> new UnsupportedLogicLocationSchemeException(scheme));
	}
}
