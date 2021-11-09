/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorfacades.email.impl;

import de.hybris.platform.acceleratorfacades.email.EmailTemplateFacade;
import de.hybris.platform.acceleratorservices.email.EmailTemplateService;
import de.hybris.platform.acceleratorservices.email.data.EmailPageData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;

import org.springframework.beans.factory.annotation.Required;


public class DefaultEmailTemplateFacade implements EmailTemplateFacade
{

	private EmailTemplateService emailTemplateService;

	@Override
	public String getPageTemplate(EmailPageData emailPageData) throws CMSItemNotFoundException
	{
		return getEmailTemplateService().getPageTemplate(emailPageData);
	}

	protected EmailTemplateService getEmailTemplateService()
	{
		return emailTemplateService;
	}

	@Required
	public void setEmailTemplateService(EmailTemplateService emailTemplateService)
	{
		this.emailTemplateService = emailTemplateService;
	}
}
