/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bcommercefacades.order;

import de.hybris.platform.commercefacades.order.QuoteFacade;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;


/**
 * @deprecated Since 6.3. Use quote functionality from commerce instead ({@link QuoteFacade}).<br/>
 *             Facade interface for quote operations.
 */
@Deprecated(since = "6.3", forRemoval = true)
public interface B2BQuoteFacade
{
	/**
	 * Returns the quote history of the current user.
	 *
	 * @param pageableData
	 *           paging information
	 * @return The quote history of the current user.
	 */
	SearchPageData<OrderHistoryData> getQuoteHistory(final PageableData pageableData);
}
