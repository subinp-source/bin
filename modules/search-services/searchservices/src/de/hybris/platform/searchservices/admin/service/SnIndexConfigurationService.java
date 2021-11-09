/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.service;

import de.hybris.platform.searchservices.admin.data.SnIndexConfiguration;

import java.util.List;
import java.util.Optional;


/**
 * Service for index configurations.
 */
public interface SnIndexConfigurationService
{
	/**
	 * Returns all the index configurations.
	 *
	 * @return the index configurations
	 */
	List<SnIndexConfiguration> getAllIndexConfigurations();

	/**
	 * Returns the index configuration for the given id.
	 *
	 * @param id
	 *           - the id
	 *
	 * @return the index configuration
	 */
	Optional<SnIndexConfiguration> getIndexConfigurationForId(String id);
}
