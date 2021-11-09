/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.core.service;



/**
 * Represents a qualifier. A qualifier can be: a language, a currency, a combination of them, etc.
 */
public interface SnQualifierType
{
	/**
	 * Returns the qualifier type id.
	 *
	 * @return the qualifier type id
	 */
	String getId();

	/**
	 * Returns the qualifier provider.
	 *
	 * @return the qualifier provider
	 */
	SnQualifierProvider getQualifierProvider();
}
