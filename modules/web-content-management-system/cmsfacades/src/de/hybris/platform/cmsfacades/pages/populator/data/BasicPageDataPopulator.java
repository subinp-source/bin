/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.pages.populator.data;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cmsfacades.common.populator.LocalizedPopulator;
import de.hybris.platform.cmsfacades.data.AbstractPageData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Required;


/**
 * Converts an {@link AbstractPageModel} page to a {@link AbstractPageData} dto
 * 
 * @deprecated since 6.6
 */
@Deprecated(since = "6.6", forRemoval = true)
public class BasicPageDataPopulator implements Populator<AbstractPageData, AbstractPageModel>
{
	private CMSAdminPageService cmsAdminPageService;
	private LocalizedPopulator localizedPopulator;

	@Override
	public void populate(final AbstractPageData source, final AbstractPageModel target) throws ConversionException
	{
		final PageTemplateModel template = getCmsAdminPageService()
				.getPageTemplateForIdFromActiveCatalogVersion(source.getTemplate());
		target.setMasterTemplate(template);
		target.setUid(source.getUid());
		target.setName(source.getName());
		target.setDefaultPage(source.getDefaultPage());
		target.setCatalogVersion(getCmsAdminPageService().getActiveCatalogVersion());
		target.setOnlyOneRestrictionMustApply(Optional.ofNullable(source.getOnlyOneRestrictionMustApply()).orElse(Boolean.FALSE));

		Optional.ofNullable(source.getTitle()) //
		.ifPresent(title -> getLocalizedPopulator().populate( //
				(locale, value) -> target.setTitle(value, locale), //
				(locale) -> title.get(getLocalizedPopulator().getLanguage(locale))));
	}

	protected CMSAdminPageService getCmsAdminPageService()
	{
		return cmsAdminPageService;
	}

	@Required
	public void setCmsAdminPageService(final CMSAdminPageService cmsAdminPageService)
	{
		this.cmsAdminPageService = cmsAdminPageService;
	}

	protected LocalizedPopulator getLocalizedPopulator()
	{
		return localizedPopulator;
	}

	@Required
	public void setLocalizedPopulator(final LocalizedPopulator localizedPopulator)
	{
		this.localizedPopulator = localizedPopulator;
	}

}
