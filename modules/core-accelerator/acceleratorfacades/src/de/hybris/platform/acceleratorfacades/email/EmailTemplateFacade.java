/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorfacades.email;

import de.hybris.platform.acceleratorservices.email.data.EmailPageData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;


/**
 * API for working with email page templates
 */
public interface EmailTemplateFacade
{

	/**
	 * Get an email page rendered as a string
	 *
	 * @param emailPageData
	 *           An email to render
	 * @return String page content
	 * @throws CMSItemNotFoundException
	 *            If emailPageData is invalid
	 */
	String getPageTemplate(EmailPageData emailPageData) throws CMSItemNotFoundException;

}
