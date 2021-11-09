/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.dao;

import de.hybris.platform.cms2.model.CMSComponentTypeModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;


public interface CMSComponentTypeDao extends Dao
{
	CMSComponentTypeModel getCMSComponentTypeByCode(String code);
}
