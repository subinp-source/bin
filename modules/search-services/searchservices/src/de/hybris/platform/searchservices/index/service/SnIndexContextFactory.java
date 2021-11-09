/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.index.service;

import de.hybris.platform.searchservices.index.SnIndexException;

/**
 * Implementations of this interface are responsible for creating instances of {@link SnIndexContext}.
 */
public interface SnIndexContextFactory
{
	/**
	 * Creates a new instance of {@link SnIndexContext}.
	 *
	 * @param indexTypeId
	 *           - the index type id
	 *
	 * @return the new instance of {@link SnIndexContext}
	 *
	 * @throws SnIndexException
	 *            if an error occurs
	 */
	SnIndexContext createIndexContext(String indexTypeId) throws SnIndexException;
}
