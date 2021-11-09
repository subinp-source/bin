/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.smarteditaddon.service.impl;

import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.commons.renderer.RendererService;
import de.hybris.platform.commons.renderer.daos.RendererTemplateDao;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.smarteditaddon.service.SmartEditEmailComponentRenderingService;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;


/**
 * Implementation of {@link SmartEditEmailComponentRenderingService} code of this class was mostly based on
 * {@link de.hybris.platform.acceleratorservices.document.factory.impl.AbstractHybrisVelocityContextFactory}
 */
public class DefaultSmartEditEmailComponentRenderingService implements SmartEditEmailComponentRenderingService
{

	private BaseSiteService baseSiteService;
	private ModelService modelService;
	private TypeService typeService;
	private CMSComponentService cmsComponentService;
	private RendererTemplateDao rendererTemplateDao;
	private RendererService rendererService;
	private SiteBaseUrlResolutionService siteBaseUrlResolutionService;

	@Override
	public String renderComponent(final AbstractCMSComponentModel component)
	{
		String result = "";
		final ComposedTypeModel componentType = getTypeService().getComposedTypeForClass(component.getClass());
		if (Boolean.TRUE.equals(component.getVisible())
				&& !getCmsComponentService().isComponentContainer(componentType.getCode()))
		{
			final BaseSiteModel site = getBaseSiteService().getCurrentBaseSite();
			final String renderTemplateCode = (site != null ? site.getUid() : "") + "-" + componentType.getCode()
					+ "-template";
			final List<RendererTemplateModel> results = getRendererTemplateDao()
					.findRendererTemplatesByCode(renderTemplateCode);
			final RendererTemplateModel renderTemplateModel = results.isEmpty() ? null : results.get(0);
			if (renderTemplateModel != null)
			{
				result = renderTemplate(component, renderTemplateModel, site);
			}
		}
		return result;

	}

	/**
	 * Render the given component template with data from the given component
	 *
	 * @param component
	 * 		A component context
	 * @param renderTemplate
	 * 		A template to be rendered
	 * @param site
	 * 		The BaseSite context
	 * @return The rendered html string
	 */
	protected String renderTemplate(final AbstractCMSComponentModel component, final RendererTemplateModel renderTemplate,
			final BaseSiteModel site)
	{

		final Map<String, Object> componentContext = new HashMap<>();
		processProperties(component, componentContext);
		// insert services for usage at jsp/vm page
		componentContext.put("urlResolutionService", getSiteBaseUrlResolutionService());
		// insert cms site
		componentContext.put("site", site);

		StringWriter result = new StringWriter();
		getRendererService().render(renderTemplate, componentContext, result);
		return result.toString();
	}

	/**
	 * Populate a componentContext with it's editor properties
	 * See {@link CMSComponentService}
	 *
	 * @param component
	 * 		A cms component
	 * @param componentContext
	 * 		A map to populate with the editor properties
	 */
	protected void processProperties(final AbstractCMSComponentModel component,
			final Map<String, Object> componentContext)
	{
		getCmsComponentService().getEditorProperties(component)
				.forEach((property) -> {
					try
					{
						final Object value = getModelService().getAttributeValue(component, property);
						componentContext.put(property, value);
					}
					catch (final AttributeNotSupportedException ignore)
					{
						// ignore
					}
				});
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

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	protected CMSComponentService getCmsComponentService()
	{
		return cmsComponentService;
	}

	@Required
	public void setCmsComponentService(final CMSComponentService cmsComponentService)
	{
		this.cmsComponentService = cmsComponentService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected RendererTemplateDao getRendererTemplateDao()
	{
		return rendererTemplateDao;
	}

	@Required
	public void setRendererTemplateDao(final RendererTemplateDao rendererTemplateDao)
	{
		this.rendererTemplateDao = rendererTemplateDao;
	}

	protected TypeService getTypeService()
	{
		return typeService;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}

	protected SiteBaseUrlResolutionService getSiteBaseUrlResolutionService()
	{
		return siteBaseUrlResolutionService;
	}

	@Required
	public void setSiteBaseUrlResolutionService(final SiteBaseUrlResolutionService siteBaseUrlResolutionService)
	{
		this.siteBaseUrlResolutionService = siteBaseUrlResolutionService;
	}
}
