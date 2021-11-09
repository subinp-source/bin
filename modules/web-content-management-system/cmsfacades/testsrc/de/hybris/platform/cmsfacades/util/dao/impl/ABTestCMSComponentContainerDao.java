/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.dao.impl;

import de.hybris.platform.cms2.model.contents.containers.ABTestCMSComponentContainerModel;


public class ABTestCMSComponentContainerDao extends AbstractCmsWebServicesDao<ABTestCMSComponentContainerModel>
{

	@Override
	protected String getQuery()
	{
		return "SELECT {pk} FROM {ABTestCMSComponentContainer} WHERE {uid}=?uid AND {catalogVersion}=?catalogVersion";
	}

}
