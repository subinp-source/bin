/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integration.cis.subscription.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.subscriptionfacades.data.ChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.OneTimeChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.RecurringChargeEntryData;

import com.hybris.cis.api.subscription.model.CisChargeEntry;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populate the CisChargeEntry with the ChargeEntryData information
 */
public class CisChargeEntryPopulator implements Populator<ChargeEntryData, CisChargeEntry>
{
	@Override
	public void populate(final ChargeEntryData source, final CisChargeEntry target) throws ConversionException
	{
		validateParameterNotNullStandardMessage("target", target);

		if (source == null)
		{
			return;
		}

		target.setChargePrice(source.getPrice().getValue());

		if (source instanceof RecurringChargeEntryData)
		{
			final RecurringChargeEntryData recurringChargeEntryData = (RecurringChargeEntryData) source;

			if (recurringChargeEntryData.getCycleEnd() == -1)
			{
				target.setNumberOfCycles(Integer.valueOf(0));
			}
			else
			{
				target.setNumberOfCycles(Integer.valueOf(recurringChargeEntryData.getCycleEnd()
						- recurringChargeEntryData.getCycleStart() + 1));
			}

			target.setOneTimeChargeTime(null);
		}
		else if (source instanceof OneTimeChargeEntryData)
		{
			final OneTimeChargeEntryData oneTimeChargeEntryData = (OneTimeChargeEntryData) source;
			target.setNumberOfCycles(Integer.valueOf(1));

			final String mappingCode;

			if ("onfirstbill".equals(oneTimeChargeEntryData.getBillingTime().getCode()))
			{
				mappingCode = "onFirstBill";
			}
			else
			{
				mappingCode = oneTimeChargeEntryData.getBillingTime().getCode();
			}

			target.setOneTimeChargeTime(mappingCode);
		}

	}


}
