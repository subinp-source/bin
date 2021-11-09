/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.dao;

import de.hybris.platform.servicelayer.internal.dao.GenericDao;
import de.hybris.platform.searchservices.model.SnIndexConfigurationModel;
import de.hybris.platform.searchservices.model.SnIndexTypeModel;

import java.util.List;
import java.util.Optional;


/**
 * The {@link SnIndexTypeModel} DAO.
 */
public interface SnIndexTypeDao extends GenericDao<SnIndexTypeModel>
{
	/**
	 * Finds the index types for the given index configuration.
	 *
	 * @param indexConfiguration
	 *           - the index configuration
	 *
	 * @return the index types
	 */
	List<SnIndexTypeModel> findIndexTypesByIndexConfiguration(final SnIndexConfigurationModel indexConfiguration);

	/**
	 * Finds the index type for the given id.
	 *
	 * @param id
	 *           - the id
	 *
	 * @return the index type
	 */
	Optional<SnIndexTypeModel> findIndexTypeById(final String id);
}
