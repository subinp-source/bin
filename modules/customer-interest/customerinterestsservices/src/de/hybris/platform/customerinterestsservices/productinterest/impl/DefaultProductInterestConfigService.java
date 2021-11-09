/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
 package de.hybris.platform.customerinterestsservices.productinterest.impl;

import de.hybris.platform.customerinterestsservices.productinterest.ProductInterestConfigService;
import de.hybris.platform.util.Config;

import java.util.Date;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;


/**
 * Service to deal with the product interests configuration.
 */
public class DefaultProductInterestConfigService implements ProductInterestConfigService
{
	private static final Logger LOG = Logger.getLogger(DefaultProductInterestConfigService.class.getName());
	private static final int DEFAULT_EXPIRY_DAY = 90;

	@Override
	public int getProductInterestExpiryDay()
	{
		int expiryDay = DEFAULT_EXPIRY_DAY;
		try
		{
			expiryDay = getConfiguredExpiryDay();

		}
		catch (final NumberFormatException e)
		{
			LOG.warn("The configuration for customer interest expiry day is invalid.It should be integral day or days.");
			if (LOG.isDebugEnabled())
			{
				LOG.info(e.getMessage());
			}

		}
		return expiryDay;
	}

	protected int getConfiguredExpiryDay()
	{
		return Config.getInt("customerinterestsservices.expiryDay", DEFAULT_EXPIRY_DAY);
	}

	@Override
	public Date getProductInterestExpiryDate(final Date creationTime)
	{
		return new DateTime(creationTime).plusDays(getProductInterestExpiryDay()).toDate();
	}

}
