/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
 package de.hybris.platform.notificationfacades.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.notificationfacades.data.SiteMessageData;
import de.hybris.platform.notificationservices.model.SiteMessageForCustomerModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


public class SiteMessageSearchPageDataPopulator
		implements Populator<SearchPageData<SiteMessageForCustomerModel>, SearchPageData<SiteMessageData>>
{

	private Converter<SiteMessageForCustomerModel, SiteMessageData> siteMessageConverter;
	@Override
	public void populate(final SearchPageData<SiteMessageForCustomerModel> source, final SearchPageData<SiteMessageData> target)
	{

		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setPagination(source.getPagination());
		target.setSorts(source.getSorts());
		target.setResults(getSiteMessageConverter().convertAll(source.getResults()));

	}

	protected Converter<SiteMessageForCustomerModel, SiteMessageData> getSiteMessageConverter()
	{
		return siteMessageConverter;
	}

	@Required
	public void setSiteMessageConverter(
			final Converter<SiteMessageForCustomerModel, SiteMessageData> siteMessageConverter)
	{
		this.siteMessageConverter = siteMessageConverter;
	}



}
