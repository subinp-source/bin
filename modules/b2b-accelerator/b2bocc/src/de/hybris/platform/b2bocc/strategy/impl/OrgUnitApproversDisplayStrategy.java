/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bocc.strategy.impl;

import de.hybris.platform.b2bocc.strategy.OrgUnitUsersDisplayStrategy;
import de.hybris.platform.b2bocc.util.SearchUtils;
import de.hybris.platform.b2bapprovalprocessfacades.company.B2BApproverFacade;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import org.springframework.beans.factory.annotation.Required;


public class OrgUnitApproversDisplayStrategy implements OrgUnitUsersDisplayStrategy
{
	protected B2BApproverFacade b2bApproverFacade;

	@Override
	public SearchPageData<CustomerData> getPagedUsersForUnit(final int currentPage, final int pageSize, final String sort,
			final String unitId)
	{
		final PageableData pageableData = SearchUtils.createPageableData(currentPage, pageSize, sort);
		return getB2bApproverFacade().getPagedApproversForUnit(pageableData, unitId);
	}

	protected B2BApproverFacade getB2bApproverFacade()
	{
		return b2bApproverFacade;
	}

	@Required
	public void setB2bApproverFacade(B2BApproverFacade b2bApproverFacade)
	{
		this.b2bApproverFacade = b2bApproverFacade;
	}
}
