/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.smarteditwebservices.configuration.service;

import de.hybris.platform.smarteditwebservices.model.SmarteditConfigurationModel;

import java.util.List;


/**
 * Provide methods for managing SmartEdit configuration information.
 */
public interface SmarteditConfigurationService
{

	/**
	 * Finds all ConfigurationData stores in the data store.
	 * 
	 * @return a list of {@link SmarteditConfigurationModel}
	 */
	List<SmarteditConfigurationModel> findAll();

	/**
	 * Create a new configuration model
	 * 
	 * @param configurationModel
	 *           the model to be saved
	 * @return tyhe model created
	 */
	SmarteditConfigurationModel create(SmarteditConfigurationModel configurationModel);

	/**
	 * Updates the configuration model represented by the uid
	 * 
	 * @param uid
	 *           is the unique identifier of this configuration
	 * @param configurationModel
	 *           - the model to be updated
	 * @return the configuration model updated
	 */
	SmarteditConfigurationModel update(String uid, SmarteditConfigurationModel configurationModel);


	/**
	 * Deletes the configuration model represented by this unique identifier
	 * 
	 * @param uid
	 *           the model' unique identifier
	 */
	void delete(String uid);

	/**
	 * Finds a configuration data bean by its unique identifier
	 * 
	 * @param uid
	 *           the configuration's unique identifier
	 * @return the {@link SmarteditConfigurationModel} instance, or null if it does not exist in the data store.
	 */
	SmarteditConfigurationModel findByKey(String uid);

}
