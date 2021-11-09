/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.dao;

import de.hybris.platform.cms2.model.contents.ContentSlotNameModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;


public interface ContentSlotNameDao extends Dao
{
	ContentSlotNameModel getContentSlotNameByName(String name);
}
