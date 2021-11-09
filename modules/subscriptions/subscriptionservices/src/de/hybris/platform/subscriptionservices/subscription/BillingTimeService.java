/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.subscription;

import de.hybris.platform.subscriptionservices.model.BillingTimeModel;

import javax.annotation.Nonnull;
import java.util.List;


/**
 * Billing Time service exposes methods to deal with billing time operations.
 * 
 * @spring.bean billingTimeService
 */
public interface BillingTimeService
{
	/**
	 * This method returns the {@link BillingTimeModel} associated with the code.
	 * 
	 * @param code
	 *           the code
	 * @return The {@link BillingTimeModel}
	 */
	@Nonnull
	BillingTimeModel getBillingTimeForCode(@Nonnull String code);

	/**
	 * This method returns all {@link BillingTimeModel}s.
	 * 
	 * @return All {@link BillingTimeModel}s.
	 */
	@Nonnull
	List<BillingTimeModel> getAllBillingTimes();
}
