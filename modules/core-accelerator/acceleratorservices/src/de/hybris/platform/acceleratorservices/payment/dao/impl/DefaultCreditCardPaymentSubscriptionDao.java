/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.payment.dao.impl;


import de.hybris.platform.acceleratorservices.model.payment.CCPaySubValidationModel;
import de.hybris.platform.acceleratorservices.payment.dao.CreditCardPaymentSubscriptionDao;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


public class DefaultCreditCardPaymentSubscriptionDao extends AbstractItemDao implements CreditCardPaymentSubscriptionDao
{
	private static final String PAYMENT_QUERY = "SELECT {p.pk} FROM {CreditCardPaymentInfo as p} "
			+ "WHERE {p.subscriptionId} = ?subscriptionId ORDER BY {" + CreditCardPaymentInfoModel.MODIFIEDTIME + "} DESC";

	private static final String SUBSCRIPTION_QUERY = "SELECT {p.pk} FROM {CCPaySubValidation as p} "
			+ "WHERE {p.subscriptionId} = ?subscriptionId";

	@Override
	public CCPaySubValidationModel findSubscriptionValidationBySubscription(final String subscriptionId)
	{
		validateParameterNotNull(subscriptionId, "subscriptionId must not be null!");

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(SUBSCRIPTION_QUERY);
		fQuery.addQueryParameter("subscriptionId", subscriptionId);

		final SearchResult<CCPaySubValidationModel> searchResult = getFlexibleSearchService().search(fQuery);
		final List<CCPaySubValidationModel> results = searchResult.getResult();

		if (results != null && results.iterator().hasNext())
		{
			return results.iterator().next();
		}

		return null;
	}

	@Override
	public CreditCardPaymentInfoModel findCreditCartPaymentBySubscription(final String subscriptionId)
	{
		validateParameterNotNull(subscriptionId, "subscriptionId must not be null!");

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(PAYMENT_QUERY);
		fQuery.addQueryParameter("subscriptionId", subscriptionId);

		final SearchResult<CreditCardPaymentInfoModel> searchResult = getFlexibleSearchService().search(fQuery);
		final List<CreditCardPaymentInfoModel> results = searchResult.getResult();

		if (results != null && results.iterator().hasNext())
		{
			return results.iterator().next();
		}

		return null;
	}
}
