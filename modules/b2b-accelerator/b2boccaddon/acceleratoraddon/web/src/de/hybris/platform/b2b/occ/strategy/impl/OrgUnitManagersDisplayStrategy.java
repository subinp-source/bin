/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.occ.strategy.impl;

import de.hybris.platform.b2b.occ.strategy.OrgUnitUsersDisplayStrategy;
import de.hybris.platform.b2b.occ.util.SearchUtils;
import de.hybris.platform.b2bcommercefacades.company.B2BUnitFacade;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import org.springframework.beans.factory.annotation.Required;


public class OrgUnitManagersDisplayStrategy implements OrgUnitUsersDisplayStrategy
{
	protected B2BUnitFacade b2bUnitFacade;

	@Override
	public SearchPageData<CustomerData> getPagedUsersForUnit(final int currentPage, final int pageSize, final String sort,
			final String unitId)
	{
		final PageableData pageableData = SearchUtils.createPageableData(currentPage, pageSize, sort);
		return getB2bUnitFacade().getPagedManagersForUnit(pageableData, unitId);
	}

	protected B2BUnitFacade getB2bUnitFacade()
	{
		return b2bUnitFacade;
	}

	@Required
	public void setB2bUnitFacade(B2BUnitFacade b2bUnitFacade)
	{
		this.b2bUnitFacade = b2bUnitFacade;
	}
}
