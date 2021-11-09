/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.email;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;


/**
 * Service for looking up the CMS email page for a template name.
 */
public interface CMSEmailPageService
{
	/**
	 * Retrieves EmailPage given its frontend template name.
	 * 
	 * @param frontendTemplateName
	 *           Frontend template name
	 * @param catalogVersion
	 *           Catalog version
	 * @return EmailPage object if found, null otherwise
	 */
	EmailPageModel getEmailPageForFrontendTemplate(String frontendTemplateName, CatalogVersionModel catalogVersion);
}
