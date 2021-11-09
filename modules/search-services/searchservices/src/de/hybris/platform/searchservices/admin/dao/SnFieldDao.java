/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.dao;

import de.hybris.platform.searchservices.model.SnFieldModel;
import de.hybris.platform.searchservices.model.SnIndexTypeModel;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;

import java.util.List;
import java.util.Optional;


/**
 * The {@link SnFieldModel} DAO.
 */
public interface SnFieldDao extends GenericDao<SnFieldModel>
{
	/**
	 * Finds the fields for the given index type.
	 *
	 * @param indexType
	 *           - the index type
	 *
	 * @return the fieldw
	 */
	List<SnFieldModel> findFieldsByIndexType(SnIndexTypeModel indexType);

	/**
	 * Finds the field for the given index type and id.
	 *
	 * @param indexType
	 *           - the index type
	 * @param id
	 *           - the id
	 *
	 * @return the field
	 */
	Optional<SnFieldModel> findFieldByIndexTypeAndId(SnIndexTypeModel indexType, String id);
}
