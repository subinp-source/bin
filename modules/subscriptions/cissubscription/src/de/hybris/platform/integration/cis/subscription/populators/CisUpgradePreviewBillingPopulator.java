/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integration.cis.subscription.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.subscriptionfacades.data.SubscriptionBillingData;

import java.util.List;

import com.hybris.cis.api.subscription.model.CisSubscriptionBillingInfo;


/**
 * Populate the {@link SubscriptionBillingData} with the {@link CisSubscriptionBillingInfo} data
 */
public class CisUpgradePreviewBillingPopulator implements
		Populator<List<CisSubscriptionBillingInfo>, List<SubscriptionBillingData>>
{
	@Override
	public void populate(final List<CisSubscriptionBillingInfo> cisSubscriptionBillings,
			final List<SubscriptionBillingData> subscriptionBillingDatas) throws ConversionException
	{
		if (cisSubscriptionBillings != null)
		{
			for (final CisSubscriptionBillingInfo cisSubscriptionBilling : cisSubscriptionBillings)
			{
				subscriptionBillingDatas.add(populate(cisSubscriptionBilling));
			}
		}
	}

	public SubscriptionBillingData populate(final CisSubscriptionBillingInfo cisSubscriptionBilling) throws ConversionException
	{
		final SubscriptionBillingData subscriptionBillingData = new SubscriptionBillingData();

		subscriptionBillingData.setBillingDate(cisSubscriptionBilling.getBillingDate());
		subscriptionBillingData.setBillingPeriod(cisSubscriptionBilling.getBillingPeriod());
		subscriptionBillingData.setPaymentAmount(cisSubscriptionBilling.getAmount());
		subscriptionBillingData.setPaymentStatus(cisSubscriptionBilling.getStatus());

		return subscriptionBillingData;
	}
}
