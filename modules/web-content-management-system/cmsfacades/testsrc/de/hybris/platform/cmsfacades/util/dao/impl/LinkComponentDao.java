/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.dao.impl;

import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;


public class LinkComponentDao extends AbstractCmsWebServicesDao<CMSLinkComponentModel>
{

	@Override
	protected String getQuery()
	{
		return "SELECT {pk} from {CMSLinkComponent} WHERE {uid}=?uid AND {catalogVersion}=?catalogVersion";
	}

}
