/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationfacades.facades;


import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.notificationfacades.data.SiteMessageData;

/**
 * Facade for site message
 */
public interface SiteMessageFacade
{

	/**
	 * get all site messages according to the given type
	 *
	 * @param type
	 *           the site message's type
	 * @param searchPageData
	 *           the search page data
	 * @return site messages
	 */
	SearchPageData<SiteMessageData> getPaginatedSiteMessagesForType(String type, SearchPageData searchPageData);

	/**
	 * get all site messages
	 * @param searchPageData
	 *           the search page data
	 * @return site messages
	 */
	SearchPageData<SiteMessageData> getPaginatedSiteMessages(SearchPageData searchPageData);

}
