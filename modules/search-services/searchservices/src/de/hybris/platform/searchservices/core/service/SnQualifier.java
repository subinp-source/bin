/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.core.service;



/**
 * Represents a qualifier. A qualifier can be: a language, a currency, a combination of them, etc.
 */
public interface SnQualifier
{
	/**
	 * Returns the qualifier id.
	 *
	 * @return the qualifier id
	 */
	String getId();

	/**
	 * Returns whether the given qualifier class is supported or not.
	 *
	 * @return <code>true</code> if the given qualifier class is supported, <code>false</code> otherwise
	 */
	boolean canGetAs(Class<?> qualifierClass);

	/**
	 * Extracts a value from this <code>Qualifier<code> object.
	 *
	 * @param qualifierClass
	 *           - the qualifier class
	 *
	 * @return the extracted value
	 *
	 * @throws IllegalArgumentException
	 *            if the qualifier class is not supported
	 */
	<Q> Q getAs(Class<Q> qualifierClass);
}
