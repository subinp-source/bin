/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.dao.impl;

import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;


public class ContentSlotForPageDao extends AbstractCmsWebServicesDao<ContentSlotForPageModel>
{

	@Override
	protected String getQuery()
	{
		return "SELECT {pk} FROM {ContentSlotForPage} WHERE {uid}=?uid AND {catalogVersion}=?catalogVersion";
	}

}
