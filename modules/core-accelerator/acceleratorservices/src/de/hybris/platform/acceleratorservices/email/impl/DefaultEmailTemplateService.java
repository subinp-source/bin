/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.email.impl;

import de.hybris.platform.acceleratorservices.email.EmailTemplateService;
import de.hybris.platform.acceleratorservices.email.data.EmailPageData;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageTemplateModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.commons.renderer.RendererService;

import java.io.StringWriter;

import org.springframework.beans.factory.annotation.Required;


public class DefaultEmailTemplateService implements EmailTemplateService
{
	private RendererService rendererService;
	private CMSPageService cmsPageService;

	@Override
	public String getPageTemplate(final EmailPageData emailPageData) throws CMSItemNotFoundException
	{
		final EmailPageModel pageForRequest = (EmailPageModel) getCmsPageService().getPageForId(emailPageData.getPageUid());
		final EmailPageTemplateModel template = (EmailPageTemplateModel) pageForRequest.getMasterTemplate();
		final RendererTemplateModel htmlTemplate = template.getHtmlTemplate();

		final StringWriter writer = new StringWriter();
		getRendererService().render(htmlTemplate, null, writer);
		return writer.toString();
	}


	protected CMSPageService getCmsPageService()
	{
		return cmsPageService;
	}

	@Required
	public void setCmsPageService(final CMSPageService cmsPageService)
	{
		this.cmsPageService = cmsPageService;
	}

	protected RendererService getRendererService()
	{
		return rendererService;
	}

	@Required
	public void setRendererService(final RendererService rendererService)
	{
		this.rendererService = rendererService;
	}

}
