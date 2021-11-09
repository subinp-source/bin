/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.dao;

import de.hybris.platform.servicelayer.internal.dao.GenericDao;
import de.hybris.platform.searchservices.model.SnIndexConfigurationModel;

import java.util.Optional;


/**
 * The {@link SnIndexConfigurationModel} DAO.
 */
public interface SnIndexConfigurationDao extends GenericDao<SnIndexConfigurationModel>
{
	/**
	 * Finds the index configuration for the given id.
	 *
	 * @param id
	 *           - the id
	 *
	 * @return the index configuration
	 */
	Optional<SnIndexConfigurationModel> findIndexConfigurationById(final String id);
}
