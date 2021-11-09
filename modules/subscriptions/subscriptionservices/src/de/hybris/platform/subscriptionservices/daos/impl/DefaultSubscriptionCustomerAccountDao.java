/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.daos.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.commerceservices.customer.dao.impl.DefaultCustomerAccountDao;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.store.BaseStoreModel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Data Access Object for looking up items related to the Customer's account. It overrides the super class' methods to
 * exclude child orders from the result set.
 *
 *
 * @deprecated since 21.05
 */
@Deprecated(since = "2105", forRemoval = true)
public class DefaultSubscriptionCustomerAccountDao extends DefaultCustomerAccountDao
{

	private static final String FIND_ORDERS_BY_CUSTOMER_STORE_QUERY = "GET {" + OrderModel._TYPECODE + "} WHERE {" + OrderModel.USER
			+ "} = ?customer AND {" + OrderModel.VERSIONID + "} IS NULL AND {" + OrderModel.STORE + "} = ?store AND {"
			+ OrderModel.PARENT + "} IS NULL";

	private static final String SORT_ORDERS_BY_DATE = " ORDER BY {" + OrderModel.CREATIONTIME + "} DESC, {" + OrderModel.PK + "}";

	private static final String SORT_ORDERS_BY_CODE = " ORDER BY {" + OrderModel.CODE + "},{" + OrderModel.CREATIONTIME
			+ "} DESC, {" + OrderModel.PK + "}";

	private static final String BY_DATE = "byDate";


	@Override
	@Nonnull
	public List<OrderModel> findOrdersByCustomerAndStore(@Nonnull final CustomerModel customerModel,
	                                                     @Nonnull final BaseStoreModel store,
														 @Nullable final OrderStatus[] status)
	{
		validateParameterNotNull(customerModel, "Customer must not be null");
		validateParameterNotNull(store, "Store must not be null");

		final Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("customer", customerModel);
		queryParams.put("store", store);

		final String query = FIND_ORDERS_BY_CUSTOMER_STORE_QUERY;
		final SearchResult<OrderModel> result = getFlexibleSearchService().search(query, queryParams);
		if (status != null && status.length > 0)
		{
			final Set<OrderStatus> orderStatusSet = Arrays.stream(status).collect(Collectors.toSet());

			return result.getResult().stream()
					.filter(orderModel -> orderStatusSet.contains(orderModel.getStatus()))
					.collect(Collectors.toList());
		}
		else
		{
			return result.getResult();
		}
	}

	@Override
	@Nonnull
	public SearchPageData<OrderModel> findOrdersByCustomerAndStore(@Nonnull final CustomerModel customerModel,
	                                                               @Nonnull final BaseStoreModel store,
																   @Nullable final OrderStatus[] status,
																   @Nonnull final PageableData pageableData)
	{
		validateParameterNotNull(customerModel, "Customer must not be null");
		validateParameterNotNull(store, "Store must not be null");

		final Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("customer", customerModel);
		queryParams.put("store", store);

		final List<SortQueryData> sortQueries = Arrays.asList(createSortQueryData(BY_DATE, FIND_ORDERS_BY_CUSTOMER_STORE_QUERY + SORT_ORDERS_BY_DATE),
				createSortQueryData("byOrderNumber", FIND_ORDERS_BY_CUSTOMER_STORE_QUERY + SORT_ORDERS_BY_CODE));

		final SearchPageData<OrderModel> result = getPagedFlexibleSearchService().search(sortQueries, BY_DATE, queryParams, pageableData);

		if (status != null && status.length > 0)
		{
			final Set<OrderStatus> orderStatusSet = Arrays.stream(status).collect(Collectors.toSet());
			final List<OrderModel> filteredByStatusResult = result.getResults().stream()
					.filter(orderModel -> orderStatusSet.contains(orderModel.getStatus()))
					.collect(Collectors.toList());

			result.setResults(filteredByStatusResult);

			return result;
		}
		else
		{
			return getPagedFlexibleSearchService().search(sortQueries, BY_DATE, queryParams, pageableData);
		}
	}

}
