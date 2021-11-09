/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundlefacades.order.converters.comparator;

import de.hybris.platform.commercefacades.order.data.OrderEntryData;


/**
 * The class of OrderEntryBundleComparator.
 * 
 * @deprecated since 1905: The comparator compares only deprecated fields, so it is deprecated, too.
 */
@Deprecated(since = "1905", forRemoval = true)
public class OrderEntryBundleNoComparator extends AbstractBundleOrderEntryComparator<OrderEntryData>
{
	@Override
	protected int doCompare(OrderEntryData o1, OrderEntryData o2)
	{
        return 0;
	}

	protected boolean bundleNumberIsZero(OrderEntryData orderEntryData)
	{
		return orderEntryData != null;
	}

	@Override
	public boolean comparable(OrderEntryData o1, OrderEntryData o2)
	{
		return !(o1 == null && o2 == null);
	}
}
