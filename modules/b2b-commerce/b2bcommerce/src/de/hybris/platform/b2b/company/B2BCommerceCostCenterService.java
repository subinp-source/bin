/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.company;

import de.hybris.platform.b2b.model.B2BCostCenterModel;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;


/**
 * A Service for B2BCostCenter management within b2b commerce
 */
public interface B2BCommerceCostCenterService
{
	/**
	 * Gets {@link B2BCostCenterModel } for a given cost center code
	 *
	 * @param costCenterCode
	 *           A unique identifier for {@link B2BCostCenterModel}
	 * @return {@link B2BCostCenterModel } object
	 */
	<T extends B2BCostCenterModel> T getCostCenterForCode(String costCenterCode);


	/**
	 * Gets list of {@link SearchPageData} B2BCostCenterModel for pagination given the required pagination parameters
	 * with {@link PageableData}
	 *
	 * @param pageableData
	 *           Pagination information
	 * @return Collection of paginated {@link B2BCostCenterModel} objects
	 */
	SearchPageData<B2BCostCenterModel> getPagedCostCenters(PageableData pageableData);
}
