/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.dao;

import de.hybris.platform.b2b.model.B2BPermissionModel;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;


/**
 * A data access object around {@link B2BPermissionModel}
 *
 */
public interface B2BPermissionDao extends GenericDao<B2BPermissionModel>
{
	/**
	 * Retrieves a {@link B2BPermissionModel} by its code.
	 *
	 * @param code
	 *           the code
	 * @return permission matching the code or null if none found
	 */
	B2BPermissionModel findPermissionByCode(final String code);
}
