/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.service;

import de.hybris.platform.searchservices.admin.data.SnSynonymDictionary;

import java.util.Optional;


/**
 * Service for synonym dictionaries.
 */
public interface SnSynonymDictionaryService
{
	/**
	 * Returns the synonym dictionary for the given id.
	 *
	 * @param id
	 *           - the id
	 *
	 * @return the synonym dictionary
	 */
	Optional<SnSynonymDictionary> getSynonymDictionaryForId(String id);
}
