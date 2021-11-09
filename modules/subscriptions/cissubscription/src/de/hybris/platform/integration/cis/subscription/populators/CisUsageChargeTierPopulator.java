/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integration.cis.subscription.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.subscriptionfacades.data.OverageUsageChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.TierUsageChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.UsageChargeEntryData;

import com.hybris.cis.api.subscription.model.CisUsageChargeTier;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populate the CisUsageChargeTierEntry with the UsageChargeEntryData information
 */
public class CisUsageChargeTierPopulator implements Populator<UsageChargeEntryData, CisUsageChargeTier>
{
	@Override
	public void populate(final UsageChargeEntryData source, final CisUsageChargeTier target) throws ConversionException
	{
		validateParameterNotNullStandardMessage("target", target);

		if (source == null)
		{
			return;
		}

		target.setChargePrice(source.getPrice().getValue());

		if (source instanceof OverageUsageChargeEntryData)
		{
			target.setNumberOfUnits(Integer.valueOf(0));
		}
		else if (source instanceof TierUsageChargeEntryData)
		{
			final TierUsageChargeEntryData tierUsageChargeEntryData = (TierUsageChargeEntryData) source;
			target.setNumberOfUnits(Integer.valueOf(tierUsageChargeEntryData.getTierEnd() - tierUsageChargeEntryData.getTierStart()));
		}
	}
}
