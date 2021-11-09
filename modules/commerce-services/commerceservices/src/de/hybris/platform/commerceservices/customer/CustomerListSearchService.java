/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.customer;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.Map;


/**
 * Customer List Service Search interface holding service layer methods for dealing with Customers
 *
 */
public interface CustomerListSearchService
{
	/**
	 * Get paginated customers for specific customer list
	 *
	 * @param customerListUid
	 *           customer list UID
	 * @param employeeUid
	 *           employee ID
	 * @param pageableData
	 *           paging information
	 * @param parameterMap
	 *           extra parameters to be provided
	 * @return customer model search page data
	 */
	<T extends CustomerModel> SearchPageData<T> getPagedCustomers(final String customerListUid, final String employeeUid,
			final PageableData pageableData, final Map<String, Object> parameterMap);

}
