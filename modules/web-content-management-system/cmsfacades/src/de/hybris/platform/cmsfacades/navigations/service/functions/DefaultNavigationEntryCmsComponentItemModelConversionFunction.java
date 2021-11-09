/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.navigations.service.functions;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminComponentService;
import de.hybris.platform.cmsfacades.data.NavigationEntryData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation for conversion of {@link NavigationEntryData} into {@link AbstractCMSComponentModel}
 * @deprecated since 1811 - no longer needed
 */
@Deprecated(since = "1811", forRemoval = true)
public class DefaultNavigationEntryCmsComponentItemModelConversionFunction
implements Function<NavigationEntryData, AbstractCMSComponentModel>
{

	private CMSAdminComponentService componentAdminService;

	@Override
	public AbstractCMSComponentModel apply(final NavigationEntryData navigationEntryData)
	{
		try
		{
			return getComponentAdminService().getCMSComponentForId(navigationEntryData.getItemId());
		}
		catch (final AmbiguousIdentifierException | UnknownIdentifierException e)
		{
			throw new ConversionException("Invalid CMS Component Item: " + navigationEntryData.getItemId(), e);
		}
	}

	protected CMSAdminComponentService getComponentAdminService()
	{
		return componentAdminService;
	}

	@Required
	public void setComponentAdminService(final CMSAdminComponentService componentAdminService)
	{
		this.componentAdminService = componentAdminService;
	}
}
