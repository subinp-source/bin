/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorcms.services;

import de.hybris.platform.acceleratorcms.data.CmsPageRequestContextData;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.data.RestrictionData;

import javax.servlet.ServletRequest;

/**
 * Service to build up the CMS Page Context data.
 * The CmsPageRequestContextData is a request scoped bean which is populated by this service.
 */
public interface CMSPageContextService
{
	/**
	 * Initialise the CmsPageRequestContextData based on the request.
	 * Sets up the CMS preview data if any, and the live edit flags if any.
	 *
	 * @param request the servlet request
	 * @return the current CmsPageRequestContextData
	 */
	CmsPageRequestContextData initialiseCmsPageContextForRequest(ServletRequest request);

	/**
	 * Update the CmsPageContext with the selected CMS page and RestrictionData.
	 *
	 * @param request the servlet request
	 * @param page the CMS page
	 * @param restrictionData the CMS Restriction data
	 * @return the current CmsPageRequestContextData
	 */
	CmsPageRequestContextData updateCmsPageContextForPage(ServletRequest request, AbstractPageModel page, RestrictionData restrictionData);

	/**
	 * Get the CmsPageRequestContextData for the current request.
	 *
	 * @param request the servlet request
	 * @return the current CmsPageRequestContextData
	 */
	CmsPageRequestContextData getCmsPageRequestContextData(ServletRequest request);
}
