/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorwebservicesaddon.payment.dao.impl;


import de.hybris.platform.acceleratorwebservicesaddon.payment.dao.PaymentSubscriptionResultDao;
import de.hybris.platform.commercewebservicescommons.model.payment.PaymentSubscriptionResultModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DefaultPaymentSubscriptionResultDao extends AbstractItemDao implements PaymentSubscriptionResultDao
{
	private static final String FIND_SUBSCRIPTION_RESULT_BY_CART =
			"SELECT {" + PaymentSubscriptionResultModel.PK + "} FROM {" + PaymentSubscriptionResultModel._TYPECODE + "} WHERE {"
					+ PaymentSubscriptionResultModel.CARTID + "} = ?cartId";
	private static final String FIND_SUBSCRIPTION_RESULT_BY_CART_CODE_AND_GUID =
			"SELECT {" + PaymentSubscriptionResultModel.PK + "} FROM {" + PaymentSubscriptionResultModel._TYPECODE + "} WHERE {"
					+ PaymentSubscriptionResultModel.CARTID + "} = ?cartCode OR {" + PaymentSubscriptionResultModel.CARTID
					+ "} = ?cartGuid";
	private static final String FIND_OLD_SUBSCRIPTION_RESULT =
			"SELECT {" + PaymentSubscriptionResultModel.PK + "} FROM {" + PaymentSubscriptionResultModel._TYPECODE + "} WHERE {"
					+ PaymentSubscriptionResultModel.MODIFIEDTIME + "} <= ?modifiedBefore";

	@Override
	public PaymentSubscriptionResultModel findPaymentSubscriptionResultByCart(final String cartId)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("cartId", cartId);
		return searchUnique(new FlexibleSearchQuery(FIND_SUBSCRIPTION_RESULT_BY_CART, params));
	}

	@Override
	public List<PaymentSubscriptionResultModel> findPaymentSubscriptionResultByCart(final String cartCode, final String cartGuid)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("cartCode", cartCode);
		params.put("cartGuid", cartGuid);

		return doSearch(FIND_SUBSCRIPTION_RESULT_BY_CART_CODE_AND_GUID, params, PaymentSubscriptionResultModel.class);
	}

	@Override
	public List<PaymentSubscriptionResultModel> findOldPaymentSubscriptionResult(final Date modifiedBefore)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("modifiedBefore", modifiedBefore);

		return doSearch(FIND_OLD_SUBSCRIPTION_RESULT, params, PaymentSubscriptionResultModel.class);
	}

	protected <T> List<T> doSearch(final String query, final Map<String, Object> params, final Class<T> resultClass)
	{
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		if (params != null)
		{
			fQuery.addQueryParameters(params);
		}

		fQuery.setResultClassList(Collections.singletonList(resultClass));

		final SearchResult<T> searchResult = search(fQuery);
		return searchResult.getResult();
	}
}
