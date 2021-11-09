/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.core.service;

import de.hybris.platform.searchservices.admin.data.SnIndexConfiguration;
import de.hybris.platform.searchservices.admin.data.SnIndexType;

import java.util.List;
import java.util.Map;


/**
 * Represents a context.
 */
public interface SnContext
{
	/**
	 * Returns the index configuration.
	 *
	 * @return the index configuration
	 */
	SnIndexConfiguration getIndexConfiguration();

	/**
	 * Returns the index type.
	 *
	 * @return the index type
	 */
	SnIndexType getIndexType();

	/**
	 * Returns the qualifiers.
	 *
	 * @return the qualifiers
	 */
	Map<String, List<SnQualifier>> getQualifiers();

	/**
	 * Returns a mutable {@link Map} that can be used to store attributes associated with this {@link SnContext}.
	 *
	 * @return the map containing the attributes
	 */
	Map<String, Object> getAttributes();

	/**
	 * Adds a new exception to this context.
	 *
	 * @param exception
	 *           - the exception
	 */
	void addException(final Exception exception);

	/**
	 * Returns the exceptions for this context.
	 *
	 * @return the exceptions for this context
	 */
	List<Exception> getExceptions();
}
