/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.dao.impl;

import de.hybris.platform.cms2.model.pages.ProductPageModel;


public class ProductPageDao extends AbstractCmsWebServicesDao<ProductPageModel>
{

	@Override
	protected String getQuery()
	{
		return "SELECT {pk} FROM {ProductPage} WHERE {uid}=?uid AND {catalogVersion}=?catalogVersion";
	}

}
