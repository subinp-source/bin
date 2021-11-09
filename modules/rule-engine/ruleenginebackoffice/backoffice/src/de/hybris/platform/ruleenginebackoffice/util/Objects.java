/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.ruleenginebackoffice.util;


import java.util.Collection;
import java.util.function.Function;


/**
 * Utility methods for objects.
 */
public class Objects
{
	private Objects()
	{
	}

	/**
	 * Checks if value is null, or empty/contains null, if value is a collection.
	 */
	public static <T> boolean nonNull(final T val) {
		return !(val == null || isEmpty(val) || containsNull(val));
	}

	/**
	 * Calls function with {@link #nonNull(Object)} parameter or returns null.
	 */
	public static <T1, T2> T2 getOrDefault(final Function<T1, T2> func, final T1 param) {
		return getOrDefault(func, param, null);
	}

	/**
	 * Calls function with {@link #nonNull(Object)} parameter or returns a default value.
	 */
	public static <T1, T2> T2 getOrDefault(final Function<T1, T2> func, final T1 param, final T2 defaultValue) {
		return nonNull(param) ? func.apply(param) : defaultValue;
	}

	private static <T> boolean containsNull(final T val)
	{
		return val instanceof Collection && ((Collection)val).stream().anyMatch(x -> !nonNull(x));
	}

	private static <T> boolean isEmpty(final T val)
	{
		return val instanceof Collection && ((Collection)val).isEmpty();
	}
}
