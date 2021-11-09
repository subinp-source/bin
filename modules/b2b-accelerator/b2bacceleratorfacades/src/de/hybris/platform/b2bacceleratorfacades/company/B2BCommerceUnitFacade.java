/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorfacades.company;

import de.hybris.platform.b2bapprovalprocessfacades.company.B2BApproverFacade;
import de.hybris.platform.b2bcommercefacades.company.B2BUnitFacade;
import de.hybris.platform.b2bcommercefacades.company.data.B2BSelectionData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;


/**
 * A facade for unit management within b2b commerce
 *
 * @deprecated Since 6.0. Use {@link B2BUnitFacade} and {@link B2BApproverFacade} instead.
 */
@Deprecated(since = "6.0", forRemoval = true)
public interface B2BCommerceUnitFacade extends B2BUnitFacade
{
	/**
	 * Gets a paged list of approvers. Approvers already assigned to the business unit with <param>unitUid</param> are
	 * marked as selected.
	 *
	 * @param pageableData
	 *           Pagination data
	 * @param unitUid
	 *           A unit id of the business unit from which to check selected approvers.
	 * @return A paged approver data.
	 *
	 * @deprecated Since 6.0. Use {@link B2BApproverFacade#getPagedApproversForUnit(PageableData, String)} instead.
	 */
	@Deprecated(since = "6.0", forRemoval = true)
	SearchPageData<CustomerData> getPagedApproversForUnit(PageableData pageableData, String unitUid);

	/**
	 * Adds an approver to a unit.
	 *
	 * @param unitUid
	 *           A unit to add an approver to
	 * @param approverUid
	 *           The approver to add to a unit's list of approvers
	 * @return An approver if added successfully otherwise null.
	 *
	 * @deprecated Since 6.0. Use {@link B2BApproverFacade#addApproverToUnit(String, String)} instead.
	 */
	@Deprecated(since = "6.0", forRemoval = true)
	B2BSelectionData addApproverToUnit(final String unitUid, final String approverUid);

	/**
	 * Removes an approver from a unit.
	 *
	 *
	 * @param unit
	 *           A business unit id
	 * @param approver
	 *           An approvers uid
	 * @return An approver
	 *
	 * @deprecated Since 6.0. Use {@link B2BApproverFacade#removeApproverFromUnit(String, String)} instead.
	 */
	@Deprecated(since = "6.0", forRemoval = true)
	B2BSelectionData removeApproverFromUnit(String unit, String approver);
}
