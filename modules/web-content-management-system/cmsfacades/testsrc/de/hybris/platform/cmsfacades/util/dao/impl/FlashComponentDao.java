/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.dao.impl;

import de.hybris.platform.cms2lib.model.components.FlashComponentModel;


public class FlashComponentDao extends AbstractCmsWebServicesDao<FlashComponentModel>
{

	@Override
	protected String getQuery()
	{
		return "SELECT {pk} FROM {FlashComponent} WHERE {uid}=?uid AND {catalogVersion}=?catalogVersion";
	}

}
