/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.dao.impl;

import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;


public class ParagraphComponentDao extends AbstractCmsWebServicesDao<CMSParagraphComponentModel>
{

	@Override
	protected String getQuery()
	{
		return "SELECT {pk} FROM {CMSParagraphComponent} WHERE {uid}=?uid AND {catalogVersion}=?catalogVersion";
	}

}
