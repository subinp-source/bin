/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorservices.dao.impl;

import de.hybris.platform.b2b.model.B2BBudgetModel;
import de.hybris.platform.b2bacceleratorservices.dao.PagedB2BBudgetDao;
import de.hybris.platform.commerceservices.search.dao.impl.DefaultPagedGenericDao;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.servicelayer.internal.dao.SortParameters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * @deprecated Since 6.0. Use {@link de.hybris.platform.b2b.dao.impl.DefaultPagedB2BBudgetDao} instead.
 */
@Deprecated(since = "6.0", forRemoval = true)
public class DefaultPagedB2BBudgetDao extends DefaultPagedGenericDao<B2BBudgetModel> implements PagedB2BBudgetDao<B2BBudgetModel>
{
	private static final String FIND_BUDGETS_BY_PARENT_UNIT = "SELECT {B2BBudget:pk}			"
			+ " FROM { B2BBudget as B2BBudget 																"
			+ " JOIN   B2BUnit 	as B2BUnit 	  ON  {B2BBudget:unit} = {B2BUnit:pk} }			"
			+ " ORDER BY {B2BUnit:name}																		";

	public DefaultPagedB2BBudgetDao(final String typeCode)
	{
		super(typeCode);
	}

	@Override
	public SearchPageData<B2BBudgetModel> findPagedBudgets(final String sortCode, final PageableData pageableData)
	{
		final List<SortQueryData> sortQueries = Arrays.asList(createSortQueryData("byUnitName", FIND_BUDGETS_BY_PARENT_UNIT),
				createSortQueryData("byName", new HashMap<String, Object>(), SortParameters.singletonAscending(B2BBudgetModel.NAME)),
				createSortQueryData("byCode", new HashMap<String, Object>(), SortParameters.singletonAscending(B2BBudgetModel.CODE)),
				createSortQueryData("byValue", new HashMap<String, Object>(),
						SortParameters.singletonAscending(B2BBudgetModel.BUDGET)));

		return getPagedFlexibleSearchService().search(sortQueries, sortCode, new HashMap<String, Object>(), pageableData);
	}
}
