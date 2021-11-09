/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.index.service;

import de.hybris.platform.searchservices.index.SnIndexException;

/**
 * Service for index related functionality.
 */
public interface SnIndexService
{
	/**
	 * Returns the default index id for the given index type id. The index might not yet exist on the target system.
	 *
	 * @param indexTypeId
	 *           - the index type id
	 *
	 * @return the default index id
	 *
	 * @throws SnIndexException
	 *            if an error occurs
	 */
	String getDefaultIndexId(String indexTypeId) throws SnIndexException;

	/**
	 * Deletes the index with the given id.
	 *
	 * @param indexTypeId
	 *           - the index type id
	 * @param indexId
	 *           - the index id
	 *
	 * @throws SnIndexException
	 *            if an error occurs
	 */
	void deleteIndexForId(String indexTypeId, String indexId) throws SnIndexException;
}
