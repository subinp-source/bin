/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.occ.v2.helper;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import de.hybris.platform.b2bcommercefacades.company.B2BBudgetFacade;
import de.hybris.platform.b2bcommercefacades.company.data.B2BBudgetData;
import de.hybris.platform.b2bcommercefacades.company.data.BudgetsData;
import de.hybris.platform.b2bcommercefacades.search.data.BudgetSearchStateData;
import de.hybris.platform.b2bwebservicescommons.dto.mycompany.BudgetListWsDTO;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import javax.annotation.Resource;


@Component
public class BudgetManagementHelper extends AbstractHelper
{
	@Resource(name = "budgetFacade")
	private B2BBudgetFacade budgetFacade;

	//@Cacheable(value = "budgetCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(true,true,'DTO',#statuses,#currentPage,#pageSize,#sort,#fields)")
	public BudgetListWsDTO searchBudget(final int currentPage, final int pageSize,
			final String sort, final String fields, final BudgetSearchStateData searchStateData)
	{
		final PageableData pageableData = createPageableData(currentPage, pageSize, sort);
		final SearchPageData<B2BBudgetData> searchPageData = budgetFacade.search(searchStateData, pageableData);

		final BudgetsData budgetsData = createBudgetsData(searchPageData);

		return getDataMapper().map(budgetsData, BudgetListWsDTO.class, fields);
	}

	protected BudgetsData createBudgetsData(final SearchPageData<B2BBudgetData> result)
	{
		final BudgetsData budgetsData = new BudgetsData();

		budgetsData.setBudgets(result.getResults());
		budgetsData.setSorts(result.getSorts());
		budgetsData.setPagination(result.getPagination());

		return budgetsData;
	}
}
