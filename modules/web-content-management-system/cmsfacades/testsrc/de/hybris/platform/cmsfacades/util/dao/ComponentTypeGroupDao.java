/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.dao;

import de.hybris.platform.cms2.model.ComponentTypeGroupModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;


public interface ComponentTypeGroupDao extends Dao
{
	ComponentTypeGroupModel getComponentTypeGroupByCode(String code);
}
