/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integration.cis.subscription.util;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.subscriptionfacades.data.SubscriptionData;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


/**
 * Comparator class for SubscriptionData. Order based on hard-coded status names and weights.
 */
public class SubscriptionDataComparator implements Comparator<SubscriptionData>
{
	public static final int STATUS_ORDER_INITIAL_CAPACITY = 4;
	public static final int CANCELLED_VALUE = 2;
	public static final int EXPIRED_VALUE = 3;

	private static final Map<String, Integer> statusOrder;
	static
	{
		statusOrder = new HashMap<>(STATUS_ORDER_INITIAL_CAPACITY);
		statusOrder.put("ACTIVE", Integer.valueOf(0));
		statusOrder.put("PAUSED", Integer.valueOf(1));
		statusOrder.put("CANCELLED", Integer.valueOf(CANCELLED_VALUE));
		statusOrder.put("EXPIRED", Integer.valueOf(EXPIRED_VALUE));
	}

	@Override
	public int compare(final SubscriptionData o1, final SubscriptionData o2)
	{
		validateParameterNotNullStandardMessage("subscriptionData o1", o1);
		validateParameterNotNullStandardMessage("subscriptionData o2", o2);

		final Integer status1 = statusOrder.get(o1.getSubscriptionStatus());
		final Integer status2 = statusOrder.get(o2.getSubscriptionStatus());

		if (status1 == null || status2 == null)
		{
			return 0; // do not compare unknown statuses, let them be equal
		}
		else
		{
			final int compareResult = status1.compareTo(status2);
			if (compareResult == 0)
			{
				if (o2.getStartDate() == null || o1.getStartDate() == null)
				{
					// It most likely won't happen, but we should check for null dates
					return 0;
				}
				// compare by start dates when statuses are equal
				return o2.getStartDate().compareTo(o1.getStartDate());
			}
			return compareResult;
		}
	}
}