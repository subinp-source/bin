/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorfacades.company;

import de.hybris.platform.b2bcommercefacades.company.B2BBudgetFacade;
import de.hybris.platform.b2bcommercefacades.company.data.B2BBudgetData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;


/**
 * @deprecated Since 6.0. Use {@link B2BBudgetFacade} instead.
 *             <p/>
 *             A facade for Budget management within b2b commerce
 */
@Deprecated(since = "6.0", forRemoval = true)
public interface B2BCommerceBudgetFacade
{
	/**
	 * @param b2bBudgetData
	 * @throws DuplicateUidException
	 */
	void addBudget(B2BBudgetData b2bBudgetData) throws DuplicateUidException;

	/**
	 * Enable or disable a budget
	 *
	 * @param b2BudgetCode
	 * @param active
	 * @throws DuplicateUidException
	 */
	void enableDisableBudget(String b2BudgetCode, boolean active) throws DuplicateUidException;


	/**
	 * Update budget details for edit operation
	 *
	 * @param b2BBudgetData
	 * @throws DuplicateUidException
	 */
	void updateBudgetDetails(B2BBudgetData b2BBudgetData) throws DuplicateUidException;
}
