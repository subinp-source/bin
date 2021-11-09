/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.organization.services;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.core.model.user.EmployeeModel;


/**
 * Interface for {@link QuoteModel} operations related to organizations.
 */
public interface OrgUnitQuoteService
{
	/**
	 * Returns all {@link QuoteModel} instances associated with at least one of the given employee's units.
	 *
	 * @param employee
	 *           an employee who is a member of an organizational unit
	 * @param pageableData
	 *           paging information
	 * @return {@link SearchPageData} containing the paged search result
	 */
	SearchPageData<QuoteModel> getQuotesForEmployee(EmployeeModel employee, PageableData pageableData);
}
