/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorservices.company;

import de.hybris.platform.b2b.model.B2BBudgetModel;
import de.hybris.platform.b2b.services.B2BBudgetService;
import de.hybris.platform.b2b.services.impl.DefaultB2BBudgetService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;


/**
 * @deprecated Since 6.0. Use {@link B2BBudgetService} instead.
 *             <p/>
 *             A service for budget management within b2b commerce
 */
@Deprecated(since = "6.0", forRemoval = true)
public interface B2BCommerceBudgetService
{
	/**
	 * @deprecated Since 6.0. Use {@link B2BBudgetService#getB2BBudgetForCode(String)} instead.
	 *             <p/>
	 *             Gets a {@link B2BBudgetModel } for a given budget code
	 *
	 * @param code
	 *           A unique identifier for {@link B2BBudgetModel }
	 * @return {@link B2BBudgetModel} object
	 */
	@Deprecated(since = "6.0", forRemoval = true)
	<T extends B2BBudgetModel> T getBudgetModelForCode(String code);

	/**
	 * @deprecated Since 6.0. Use {@link DefaultB2BBudgetService#findPagedBudgets(PageableData)} instead.
	 *             <p/>
	 *             Gets list of {@link SearchPageData} for pagination given the required pagination parameters with
	 *             {@link PageableData}
	 *
	 * @param pageableData
	 *           Pagination information
	 * @return List of paginated {@link B2BBudgetModel} objects
	 */
	@Deprecated(since = "6.0", forRemoval = true)
	SearchPageData<B2BBudgetModel> findPagedBudgets(PageableData pageableData);
}
