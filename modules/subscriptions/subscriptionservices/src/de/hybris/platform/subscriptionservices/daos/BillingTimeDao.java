/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.daos;

import de.hybris.platform.subscriptionservices.model.BillingTimeModel;

import javax.annotation.Nonnull;
import java.util.List;


/**
 * Data Access Object for looking up items related to {@link BillingTimeModel}.
 * 
 * @spring.bean billingTimeDao
 */
public interface BillingTimeDao
{
	/**
	 * Find all {@link BillingTimeModel}s.
	 * 
	 * @return {@link List} of {@link BillingTimeModel}s or empty {@link List}.
	 */
	@Nonnull
	List<BillingTimeModel> findAllBillingTimes();

	/**
	 * Finds the {@link BillingTimeModel} for the given code.
	 * 
	 * @param code
	 *           the code of the {@link BillingTimeModel}.
	 * @return {@link BillingTimeModel} if the given <code>code</code> was found
	 * @throws de.hybris.platform.servicelayer.exceptions.ModelNotFoundException
	 *            if nothing was found
	 * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
	 *            if by the given search parameters too many models where found
	 */
	@Nonnull
	BillingTimeModel findBillingTimeByCode(@Nonnull String code);

}
