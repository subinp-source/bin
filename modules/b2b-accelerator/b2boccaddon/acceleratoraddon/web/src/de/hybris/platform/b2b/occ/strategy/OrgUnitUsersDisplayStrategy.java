/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.occ.strategy;

import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;


public interface OrgUnitUsersDisplayStrategy
{
	/**
	 * Displays users based on a role in the unit
	 *
	 * @param currentPage is the current page which is returned
	 * @param pageSize    is the page size
	 * @param sort        is the sorting method
	 * @param unitId      is the identifier of the unit
	 * @return search page data which contains users
	 */
	SearchPageData<CustomerData> getPagedUsersForUnit(final int currentPage, final int pageSize, final String sort,
			final String unitId);
}
