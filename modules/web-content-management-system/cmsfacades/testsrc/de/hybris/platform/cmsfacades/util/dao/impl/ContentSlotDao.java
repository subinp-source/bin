/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.dao.impl;

import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;


public class ContentSlotDao extends AbstractCmsWebServicesDao<ContentSlotModel>
{

	@Override
	protected String getQuery()
	{
		return "SELECT {pk} FROM {ContentSlot} WHERE {uid}=?uid AND {catalogVersion}=?catalogVersion";
	}

}
