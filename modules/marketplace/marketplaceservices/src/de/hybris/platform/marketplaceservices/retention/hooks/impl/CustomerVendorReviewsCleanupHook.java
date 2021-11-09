/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.retention.hooks.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.marketplaceservices.model.CustomerVendorReviewModel;
import de.hybris.platform.marketplaceservices.vendor.daos.CustomerVendorReviewDao;
import de.hybris.platform.retention.hook.ItemCleanupHook;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collection;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Cleans up customer vendor reviews after the customer account is closed and the customer related data needs to be
 * cleaned. It's implementation of {@link ItemCleanupHook}
 */
public class CustomerVendorReviewsCleanupHook implements ItemCleanupHook<CustomerModel>
{

	private ModelService modelService;
	private CustomerVendorReviewDao customerVendorReviewDao;


	@Override
	public void cleanupRelatedObjects(final CustomerModel customer)
	{
		validateParameterNotNullStandardMessage("customer", customer);

		final Collection<CustomerVendorReviewModel> vendorReviews = getCustomerVendorReviewDao().findReviewsByUser(customer);
		if (CollectionUtils.isNotEmpty(vendorReviews))
		{
			getModelService().removeAll(vendorReviews);
		}
	}


	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected CustomerVendorReviewDao getCustomerVendorReviewDao()
	{
		return customerVendorReviewDao;
	}

	@Required
	public void setCustomerVendorReviewDao(final CustomerVendorReviewDao customerVendorReviewDao)
	{
		this.customerVendorReviewDao = customerVendorReviewDao;
	}

}
