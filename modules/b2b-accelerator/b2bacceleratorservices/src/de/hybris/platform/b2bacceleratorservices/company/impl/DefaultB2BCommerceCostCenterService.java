/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorservices.company.impl;

import de.hybris.platform.b2b.model.B2BCostCenterModel;
import de.hybris.platform.b2bacceleratorservices.company.B2BCommerceCostCenterService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;


/**
 *
 * @deprecated Use Since 6.0. {@link de.hybris.platform.b2b.company.impl.DefaultB2BCommerceCostCenterService} instead.
 */
@Deprecated(since = "6.0", forRemoval = true)
public class DefaultB2BCommerceCostCenterService extends DefaultCompanyB2BCommerceService implements B2BCommerceCostCenterService
{
	@Override
	public <T extends B2BCostCenterModel> T getCostCenterForCode(final String costCenterCode)
	{
		return (T) getB2BCostCenterService().getCostCenterForCode(costCenterCode);
	}


	@Override
	public SearchPageData<B2BCostCenterModel> getPagedCostCenters(final PageableData pageableData)
	{
		return getPagedB2BCostCenterDao().find(pageableData);
	}
}
