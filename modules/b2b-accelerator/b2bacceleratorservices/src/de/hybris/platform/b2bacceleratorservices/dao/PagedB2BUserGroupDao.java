/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorservices.dao;

import de.hybris.platform.b2b.model.B2BUserGroupModel;
import de.hybris.platform.commerceservices.search.dao.PagedGenericDao;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;



/**
 * @deprecated Since 6.0. Use {@link PagedGenericDao} directly instead.
 * @param <M>
 */
@Deprecated(since = "6.0", forRemoval = true)
public interface PagedB2BUserGroupDao<M> extends PagedGenericDao<M>
{
	/**
	 * Finds all visible {@link B2BUserGroupModel} within a sessions branch 2 sorts are available by default, sortCode
	 * "byName" and "byDate"
	 *
	 * @param sortCode
	 * @param pageableData
	 * @return A paged result of customers
	 */
	SearchPageData<B2BUserGroupModel> findPagedB2BUserGroup(String sortCode, PageableData pageableData);

}
