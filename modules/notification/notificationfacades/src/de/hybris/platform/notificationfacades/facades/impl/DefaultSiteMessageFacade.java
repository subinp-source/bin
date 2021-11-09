/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationfacades.facades.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.notificationfacades.data.SiteMessageData;
import de.hybris.platform.notificationfacades.facades.SiteMessageFacade;
import de.hybris.platform.notificationservices.enums.SiteMessageType;
import de.hybris.platform.notificationservices.model.SiteMessageForCustomerModel;
import de.hybris.platform.notificationservices.service.SiteMessageService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.user.UserService;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link SiteMessageFacade}
 */
public class DefaultSiteMessageFacade implements SiteMessageFacade
{

	private UserService userService;
	private SiteMessageService siteMessageService;

	private Converter<SearchPageData<SiteMessageForCustomerModel>, SearchPageData<SiteMessageData>> siteMessageSearchPageDataConverter;

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	protected SiteMessageService getSiteMessageService()
	{
		return siteMessageService;
	}


	@Override
	public SearchPageData<SiteMessageData> getPaginatedSiteMessagesForType(final String type, final SearchPageData searchPageData)
	{
		return getSiteMessageSearchPageDataConverter().convert(getSiteMessageService().getPaginatedMessagesForType(
				(CustomerModel) getUserService().getCurrentUser(), SiteMessageType.valueOf(type), searchPageData));
	}

	@Override
	public SearchPageData<SiteMessageData> getPaginatedSiteMessages(final SearchPageData searchPageData)
	{
		return getSiteMessageSearchPageDataConverter().convert(
				getSiteMessageService().getPaginatedMessagesForCustomer((CustomerModel) getUserService().getCurrentUser(), searchPageData));
	}

	@Required
	public void setSiteMessageService(final SiteMessageService siteMessageService)
	{
		this.siteMessageService = siteMessageService;
	}

	@Required
	public void setSiteMessageSearchPageDataConverter(
			final Converter<SearchPageData<SiteMessageForCustomerModel>, SearchPageData<SiteMessageData>> converter)
	{
		this.siteMessageSearchPageDataConverter = converter;
	}

	protected Converter<SearchPageData<SiteMessageForCustomerModel>, SearchPageData<SiteMessageData>> getSiteMessageSearchPageDataConverter()
	{
		return siteMessageSearchPageDataConverter;
	}

}
