/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.email;

import de.hybris.platform.acceleratorservices.email.data.EmailPageData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;


/**
 * Service designed to render email page templates
 */
public interface EmailTemplateService
{
	/**
	 * @param emailPageData
	 *           pageData
	 * @return Returns fully processed HTML body for the template provided by {@link EmailPageData}
	 * @throws CMSItemNotFoundException
	 *            If the page represented by emailPageData is not found
	 */
	String getPageTemplate(EmailPageData emailPageData) throws CMSItemNotFoundException;
}
