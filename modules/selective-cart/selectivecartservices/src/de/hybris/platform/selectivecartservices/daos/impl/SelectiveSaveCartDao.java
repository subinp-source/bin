/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.selectivecartservices.daos.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.order.dao.impl.DefaultSaveCartDao;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Implementation of {@link DefaultSaveCartDao} for selective cart
 */
public class SelectiveSaveCartDao extends DefaultSaveCartDao
{
	private static final String VISIBLE = "visible";

	private static final String FIND_SAVED_CARTS_FOR_USER_AND_SITE = SELECTCLAUSE + "WHERE {" + CartModel.USER
			+ "} = ?user AND {" + CartModel.SITE + "} = ?site AND {" + CartModel.VISIBLE + "} = ?" + VISIBLE + " AND "
			+ SAVED_CARTS_CLAUSE + " ";

	private static final String FIND_SAVED_CARTS_FOR_USER = SELECTCLAUSE + "WHERE {" + CartModel.USER + "} = ?user AND {"
			+ CartModel.VISIBLE + "} = ?" + VISIBLE + " AND " + SAVED_CARTS_CLAUSE + " ";

	@Override
	public Integer getSavedCartsCountForSiteAndUser(final BaseSiteModel baseSite, final UserModel user)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("user", user);
		params.put(VISIBLE, Boolean.TRUE);

		final String query;
		if (baseSite != null)
		{
			params.put("site", baseSite);
			query = FIND_SAVED_CARTS_FOR_USER_AND_SITE;
		}
		else
		{
			query = FIND_SAVED_CARTS_FOR_USER;
		}

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(params);
		fQuery.setResultClassList(Collections.singletonList(PK.class));

		final SearchResult<Integer> searchResult = search(fQuery);

		return searchResult.getCount();
	}

	@Override
	public SearchPageData<CartModel> getSavedCartsForSiteAndUser(final PageableData pageableData, final BaseSiteModel baseSite,
			final UserModel user, final List<OrderStatus> orderStatus)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("user", user);
		params.put(VISIBLE, Boolean.TRUE);

		final List<OrderStatus> requestedStatuses = Optional.ofNullable(orderStatus).orElseGet(List::of).stream()
				.filter(Objects::nonNull).collect(Collectors.toList());
		final String query;
		if (baseSite != null)
		{
			params.put("site", baseSite);
			if (!requestedStatuses.isEmpty())
			{
				query = adjustQueryAndParamsForGivenStatuses(FIND_SAVED_CARTS_FOR_SITE_AND_USER_WITH_STATUS, params,
						requestedStatuses);
			}
			else
			{
				query = FIND_SAVED_CARTS_FOR_USER_AND_SITE;
			}
		}
		else
		{
			if (!requestedStatuses.isEmpty())
			{
				query = adjustQueryAndParamsForGivenStatuses(FIND_SAVED_CARTS_FOR_USER_WITH_STATUS, params, requestedStatuses);
			}
			else
			{
				query = FIND_SAVED_CARTS_FOR_USER;
			}
		}

		final List<SortQueryData> sortQueries = Arrays.asList(
				createSortQueryData(SORT_CODE_BY_DATE_MODIFIED, query + ORDERBYCLAUSE),
				createSortQueryData(SORT_CODE_BY_DATE_SAVED, query + SORT_SAVED_CARTS_BY_DATE_SAVED),
				createSortQueryData(SORT_CODE_BY_NAME, query + SORT_SAVED_CARTS_BY_NAME),
				createSortQueryData(SORT_CODE_BY_CODE, query + SORT_SAVED_CARTS_BY_CODE),
				createSortQueryData(SORT_CODE_BY_TOTAL, query + SORT_SAVED_CARTS_BY_TOTAL));

		return getPagedFlexibleSearchService().search(sortQueries, SORT_CODE_BY_DATE_MODIFIED, params, pageableData);
	}
}