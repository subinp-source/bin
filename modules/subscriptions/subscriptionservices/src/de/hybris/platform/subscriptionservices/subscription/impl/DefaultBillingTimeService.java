/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.subscription.impl;

import de.hybris.platform.subscriptionservices.daos.BillingTimeDao;
import de.hybris.platform.subscriptionservices.model.BillingTimeModel;
import de.hybris.platform.subscriptionservices.subscription.BillingTimeService;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import javax.annotation.Nonnull;


/**
 * Default implementation of {@link BillingTimeService}.
 * 
 */
public class DefaultBillingTimeService implements BillingTimeService
{
	private BillingTimeDao billingTimeDao;

	@Override
	@Nonnull
	public List<BillingTimeModel> getAllBillingTimes()
	{
		return getBillingTimeDao().findAllBillingTimes();
	}

	@Override
	@Nonnull
	public BillingTimeModel getBillingTimeForCode(@Nonnull final String code)
	{
		return getBillingTimeDao().findBillingTimeByCode(code);
	}

	protected BillingTimeDao getBillingTimeDao()
	{
		return billingTimeDao;
	}

	@Required
	public void setBillingTimeDao(final BillingTimeDao billingTimeDao)
	{
		this.billingTimeDao = billingTimeDao;
	}

}
