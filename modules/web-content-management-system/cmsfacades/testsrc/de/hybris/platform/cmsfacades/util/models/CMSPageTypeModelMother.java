/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.models;

import de.hybris.platform.cms2.model.CMSPageTypeModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cmsfacades.util.dao.CMSPageTypeDao;

import org.springframework.beans.factory.annotation.Required;


public class CMSPageTypeModelMother extends AbstractModelMother<CMSPageTypeModel>
{
	protected static final Class<?> SUPER_TYPE = CMSItemModel.class;
	public static final String CODE_CONTENT_PAGE = "ContentPage";
	protected static final String CODE_CATALOG_PAGE = "CatalogPage";

	private CMSPageTypeDao cmsPageTypeDao;

	public CMSPageTypeModel ContentPage()
	{
		return cmsPageTypeDao.getCMSPageTypeByCode(CODE_CONTENT_PAGE);
	}

	public CMSPageTypeModel CatalogPage()
	{
		return cmsPageTypeDao.getCMSPageTypeByCode(CODE_CATALOG_PAGE);
	}

	protected CMSPageTypeDao getCmsPageTypeDao()
	{
		return cmsPageTypeDao;
	}

	@Required
	public void setCmsPageTypeDao(final CMSPageTypeDao cmsPageTypeDao)
	{
		this.cmsPageTypeDao = cmsPageTypeDao;
	}

}
