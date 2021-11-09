/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.core.service;

import de.hybris.platform.searchservices.core.SnException;


/**
 * Implementations of this interface are responsible for creating instances of {@link SnContext}.
 *
 * @param <T>
 *           - the context type
 */
public interface SnContextFactory
{
	/**
	 * Creates a new instance of {@link SnContext}.
	 *
	 * @param indexTypeId
	 *           - the index type id
	 *
	 * @return the new instance of {@link SnContext}
	 *
	 * @throws SnException
	 *            if an error occurs
	 */
	SnContext createContext(String indexTypeId) throws SnException;
}
