/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.service;

import de.hybris.platform.searchservices.admin.data.SnIndexType;

import java.util.List;
import java.util.Optional;


/**
 * Service for index types.
 */
public interface SnIndexTypeService
{
	/**
	 * Returns all the index types.
	 *
	 * @return the index types
	 */
	List<SnIndexType> getAllIndexTypes();

	/**
	 * Returns the index types for the given index configuration.
	 *
	 * @param indexConfigurationId
	 *           - the index configuration id
	 *
	 * @return the index types
	 */
	List<SnIndexType> getIndexTypesForIndexConfiguration(String indexConfigurationId);

	/**
	 * Returns the index type for the given id.
	 *
	 * @param id
	 *           - the id
	 *
	 * @return the index type
	 */
	Optional<SnIndexType> getIndexTypeForId(String id);
}
