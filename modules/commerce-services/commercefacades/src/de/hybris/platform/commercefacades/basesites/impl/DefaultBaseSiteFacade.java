/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.basesites.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.basesite.data.BaseSiteData;
import de.hybris.platform.commercefacades.basesites.BaseSiteFacade;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.site.BaseSiteService;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link BaseSiteFacade}
 */
public class DefaultBaseSiteFacade implements BaseSiteFacade
{
	private BaseSiteService baseSiteService;
	private Converter<BaseSiteModel, BaseSiteData> baseSiteConverter;

	@Override
	public List<BaseSiteData> getAllBaseSites()
	{
		final Collection<BaseSiteModel> allBaseSites = getBaseSiteService().getAllBaseSites();
		return getBaseSiteConverter().convertAll(allBaseSites);
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

	protected Converter<BaseSiteModel, BaseSiteData> getBaseSiteConverter()
	{
		return baseSiteConverter;
	}

	@Required
	public void setBaseSiteConverter(final Converter<BaseSiteModel, BaseSiteData> baseSiteConverter)
	{
		this.baseSiteConverter = baseSiteConverter;
	}

}
