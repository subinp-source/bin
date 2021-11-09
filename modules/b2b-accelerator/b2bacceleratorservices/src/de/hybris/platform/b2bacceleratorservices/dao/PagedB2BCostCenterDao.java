/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorservices.dao;

import de.hybris.platform.b2b.model.B2BCostCenterModel;
import de.hybris.platform.commerceservices.search.dao.PagedGenericDao;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;



/**
 * @deprecated Since 6.0. Use {@link PagedGenericDao} directly instead.
 * @param <M>
 */
@Deprecated(since = "6.0", forRemoval = true)
public interface PagedB2BCostCenterDao<M> extends PagedGenericDao<M>
{
	/**
	 * @param sortCode
	 * @param pageableData
	 * @return SearchPageData<B2BCustomerModel>
	 */
	SearchPageData<B2BCostCenterModel> findPagedCostCenters(String sortCode, PageableData pageableData);
}
