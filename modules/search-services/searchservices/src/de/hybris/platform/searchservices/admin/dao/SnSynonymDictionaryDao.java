/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.dao;

import de.hybris.platform.servicelayer.internal.dao.GenericDao;
import de.hybris.platform.searchservices.model.SnFieldModel;
import de.hybris.platform.searchservices.model.SnSynonymDictionaryModel;

import java.util.Optional;


/**
 * The {@link SnFieldModel} DAO.
 */
public interface SnSynonymDictionaryDao extends GenericDao<SnSynonymDictionaryModel>
{
	/**
	 * Finds the synonym dictionary for the given id.
	 *
	 * @param id
	 *           - the id
	 *
	 * @return the synonym dictionary
	 */
	Optional<SnSynonymDictionaryModel> findSynonymDictionaryById(String id);
}
