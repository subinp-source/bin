/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.dao.impl;

import de.hybris.platform.cms2.model.relations.ContentSlotForTemplateModel;


public class ContentSlotForTemplateDao extends AbstractCmsWebServicesDao<ContentSlotForTemplateModel>
{

	@Override
	protected String getQuery()
	{
		return "SELECT {pk} FROM {ContentSlotForTemplate} WHERE {uid}=?uid AND {catalogVersion}=?catalogVersion";
	}

}
