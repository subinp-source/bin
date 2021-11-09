/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorservices.company;

import de.hybris.platform.b2b.model.B2BPermissionModel;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;


/**
 * A service for permission management within b2b commerce.
 *
 * @deprecated Since 6.0. use {@link de.hybris.platform.b2b.company.B2BCommercePermissionService} instead.
 */
@Deprecated(since = "6.0", forRemoval = true)
public interface B2BCommercePermissionService extends de.hybris.platform.b2b.company.B2BCommercePermissionService
{
	/**
	 * Gets list of {@link SearchPageData} B2BPermissionModel for pagination given the required pagination parameters
	 * with {@link PageableData}
	 *
	 * @param pageableData
	 *           Pagination information
	 * @return Collection of paginated {@link B2BPermissionModel} objects
	 */
	SearchPageData<B2BPermissionModel> getPagedPermissions(PageableData pageableData);
}
