/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customerinterestsfacades.productinterest.populators;


import de.hybris.platform.converters.Populator;
import de.hybris.platform.customerinterestsfacades.data.ProductInterestEntryData;
import de.hybris.platform.customerinterestsservices.productinterest.ProductInterestConfigService;
import de.hybris.platform.notificationservices.enums.NotificationType;

import java.util.Date;
import java.util.Map.Entry;

import org.springframework.util.Assert;


public class ProductInterestEntryPopulator
		implements Populator<Entry<NotificationType, Date>, ProductInterestEntryData>
{
	private final ProductInterestConfigService productInterestConfigService;

	public ProductInterestEntryPopulator(final ProductInterestConfigService productInterestConfigService)
	{
		this.productInterestConfigService = productInterestConfigService;
	}

	@Override
	public void populate(final Entry<NotificationType, Date> source, final ProductInterestEntryData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setInterestType(source.getKey().name());
		target.setDateAdded(source.getValue());
		target.setExpirationDate(productInterestConfigService.getProductInterestExpiryDate(source.getValue()));

	}

}
