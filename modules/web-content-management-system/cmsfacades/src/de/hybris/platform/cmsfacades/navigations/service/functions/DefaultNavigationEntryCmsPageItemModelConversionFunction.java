/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.navigations.service.functions;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cmsfacades.data.NavigationEntryData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation for conversion of {@link NavigationEntryData} into {@link AbstractPageModel}
 * @deprecated since 1811 - no longer needed
 */
@Deprecated(since = "1811", forRemoval = true)
public class DefaultNavigationEntryCmsPageItemModelConversionFunction implements Function<NavigationEntryData, AbstractPageModel>
{

	private CMSAdminPageService cmsAdminPageService;

	@Override
	public AbstractPageModel apply(final NavigationEntryData navigationEntryData)
	{
		try
		{
			return getCmsAdminPageService().getPageForIdFromActiveCatalogVersion(navigationEntryData.getItemId());
		}
		catch (final AmbiguousIdentifierException | UnknownIdentifierException e)
		{
			throw new ConversionException("Invalid CMS Page Item: " + navigationEntryData.getItemId(), e);
		}
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
}
