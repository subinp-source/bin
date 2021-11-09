/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.dao.impl;

import de.hybris.platform.b2b.constants.B2BConstants;
import de.hybris.platform.b2b.model.B2BCostCenterModel;
import de.hybris.platform.commerceservices.search.dao.impl.DefaultPagedGenericDao;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.servicelayer.internal.dao.SortParameters;
import de.hybris.platform.util.Config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class DefaultPagedB2BCostCenterDao extends DefaultPagedGenericDao<B2BCostCenterModel>
{
	private static final String FIND_COSTCENTERS_BY_PARENT_UNIT = "SELECT {B2BCostCenter:pk} FROM { B2BCostCenter as B2BCostCenter"
			+ " JOIN B2BUnit as B2BUnit ON {B2BCostCenter:unit} = {B2BUnit:pk} } ORDER BY {B2BUnit:name}";

	private static final String DEFAULT_SORT_CODE = Config.getString(B2BConstants.DEFAULT_SORT_CODE_PROP, "byName");


	public DefaultPagedB2BCostCenterDao(final String typeCode)
	{
		super(typeCode);
	}


	@Override
	public SearchPageData<B2BCostCenterModel> find(final PageableData pageableData)
	{
		final List<SortQueryData> sortQueries = Arrays.asList(createSortQueryData("byUnitName", FIND_COSTCENTERS_BY_PARENT_UNIT),
				createSortQueryData("byName", new HashMap<String, Object>(),
						SortParameters.singletonAscending(B2BCostCenterModel.NAME)),
				createSortQueryData("byCode", new HashMap<String, Object>(),
						SortParameters.singletonAscending(B2BCostCenterModel.CODE)));

		return getPagedFlexibleSearchService().search(sortQueries, DEFAULT_SORT_CODE, new HashMap<String, Object>(), pageableData);
	}
}
