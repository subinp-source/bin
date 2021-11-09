/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorfacades.company;

import de.hybris.platform.b2bcommercefacades.company.B2BCostCenterFacade;
import de.hybris.platform.b2bcommercefacades.company.data.B2BBudgetData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BCostCenterData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BSelectionData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;


/**
 * @deprecated Since 6.0. Use {@link B2BCostCenterFacade} instead.
 *             <p/>
 *             A facade for cost center management within b2b commerce
 */
@Deprecated(since = "6.0", forRemoval = true)
public interface B2BCommerceCostCenterFacade
{
	/**
	 * Gets paginated list of Cost Centers visible to the current users branch.
	 *
	 * @param pageableData
	 *           Pagination data
	 * @return a paginated list of cost centers.
	 */
	SearchPageData<B2BCostCenterData> getPagedCostCenters(PageableData pageableData);

	/**
	 * Get view details for a given Cost center code
	 *
	 * @param costCenterCode
	 * @return B2BCostCenterData
	 */
	B2BCostCenterData getCostCenterDataForCode(String costCenterCode);

	/**
	 * Update the cost center details for edit cost centers flow
	 *
	 * @param b2BCostCenterData
	 *           {@link B2BCostCenterData}
	 * @throws {@link
	 *            DuplicateUidException}
	 */
	void updateCostCenterDetails(B2BCostCenterData b2BCostCenterData) throws DuplicateUidException;

	/**
	 * Add cost center
	 *
	 * @param b2BCostCenterData
	 *           {@link B2BCostCenterData}
	 * @throws {@link
	 *            DuplicateUidException}
	 */
	void addCostCenter(B2BCostCenterData b2BCostCenterData) throws DuplicateUidException;

	/**
	 * Enable/disable for a cost center. active set to true denotes enabling cost center and vice versa.
	 *
	 * @param costCenterCode
	 * @param active
	 * @throws {@link
	 *            DuplicateUidException}
	 */
	void enableDisableCostCenter(String costCenterCode, boolean active) throws DuplicateUidException;

	/**
	 * Retrieves paged budgets for given cost center
	 *
	 * @param pageableData
	 *           for selecting budgets
	 * @param costCenterCode
	 *           to check
	 * @return the resulting {@link SearchPageData}
	 */
	SearchPageData<B2BBudgetData> getPagedBudgetsForCostCenters(PageableData pageableData, String costCenterCode);

	/**
	 * Select a budget for cost center
	 *
	 * @param costCenterCode
	 * @param budgetCode
	 * @return the resulting {@link B2BSelectionData}
	 * @throws {@link
	 *            DuplicateUidException}
	 */
	B2BSelectionData selectBudgetForCostCenter(String costCenterCode, String budgetCode) throws DuplicateUidException;

	/**
	 * Deselect a budget for a cost center
	 *
	 * @param costCenterCode
	 * @param budgetCode
	 * @return the resulting {@link B2BSelectionData}
	 * @throws {@link
	 *            DuplicateUidException}
	 */
	B2BSelectionData deSelectBudgetForCostCenter(String costCenterCode, String budgetCode) throws DuplicateUidException;
}
