/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.customer.strategies;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.Map;


/**
 * Customer list search strategy holding methods responsible for doing the actual search for customer lists
 *
 */
public interface CustomerListSearchStrategy
{

	/**
	 * Gets customer data list based on specific implementation
	 *
	 * @param customerListUid
	 *           customer list Uid to fetch
	 * @param employeeUid
	 *           the employee Uid
	 * @param pageableData
	 *           paging information to return the data in a paginated fashion
	 * @param parameterMap
	 *           extra parameters supplied for this call
	 * @return list of customer data
	 */
	<T extends CustomerModel> SearchPageData<T> getPagedCustomers(final String customerListUid, final String employeeUid,
			final PageableData pageableData, Map<String, Object> parameterMap);

}
