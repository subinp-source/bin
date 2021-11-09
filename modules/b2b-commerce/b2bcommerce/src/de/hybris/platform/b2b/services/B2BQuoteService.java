/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.services;

import de.hybris.platform.commerceservices.order.CommerceQuoteService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.store.BaseStoreModel;


/**
 * @deprecated Since 6.3. Use quote functionality from commerce instead. ({@link CommerceQuoteService}).<br/>
 *             Service interface for quote related operations.
 */
@Deprecated(since = "6.3", forRemoval = true)
public interface B2BQuoteService
{
	/**
	 * Returns a paged list of quotes for the given customer and base store.
	 *
	 * @param customerModel
	 *           the customer to retrieve orders for
	 * @param store
	 *           the current base store
	 * @param pageableData
	 *           pagination information
	 * @return the list of quotes
	 */
	SearchPageData<OrderModel> getQuoteList(CustomerModel customerModel, BaseStoreModel store, PageableData pageableData);
}
