/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.smarteditwebservices.configuration.dao;

import de.hybris.platform.smarteditwebservices.model.SmarteditConfigurationModel;

import java.util.List;


/**
 * Interface for SmartEdit Configuration DAO
 */
public interface SmarteditConfigurationDao
{
	/**
	 * Loads all configurations persisted
	 * @return a list of {@link SmarteditConfigurationModel}
	 */
	List<SmarteditConfigurationModel> loadAll();

	/**
	 * Finds a {@link SmarteditConfigurationModel} by its key value.
	 * @param key the configuration key
	 * @return a {@link SmarteditConfigurationModel} instance, or null if it does not exist in the database
	 */
	SmarteditConfigurationModel findByKey(String key);

}
