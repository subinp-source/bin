/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorservices.company.impl;

import de.hybris.platform.b2b.model.B2BBudgetModel;
import de.hybris.platform.b2b.services.impl.DefaultB2BBudgetService;
import de.hybris.platform.b2bacceleratorservices.company.B2BCommerceBudgetService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;


/**
 * @deprecated Since 6.0. Use {@link DefaultB2BBudgetService} instead.
 *             <p/>
 *             Default implementation of {@link B2BCommerceBudgetService }
 *
 */
@Deprecated(since = "6.0", forRemoval = true)
public class DefaultB2BCommerceBudgetService extends DefaultCompanyB2BCommerceService implements B2BCommerceBudgetService
{
	@Override
	public <T extends B2BBudgetModel> T getBudgetModelForCode(final String code)
	{
		return (T) getB2BBudgetService().getB2BBudgetForCode(code);
	}

	@Override
	public SearchPageData<B2BBudgetModel> findPagedBudgets(final PageableData pageableData)
	{
		return getPagedB2BBudgetDao().find(pageableData);
	}
}
