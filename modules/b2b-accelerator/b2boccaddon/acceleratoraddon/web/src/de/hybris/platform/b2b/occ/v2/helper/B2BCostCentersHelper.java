/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.occ.v2.helper;

import de.hybris.platform.b2bcommercefacades.company.B2BBudgetFacade;
import de.hybris.platform.b2bcommercefacades.company.B2BCostCenterFacade;
import de.hybris.platform.b2bcommercefacades.company.B2BUnitFacade;
import de.hybris.platform.b2bcommercefacades.company.data.B2BBudgetData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BCostCenterData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BSelectionData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.b2bcommercefacades.company.data.BudgetsData;
import de.hybris.platform.b2bcommercefacades.search.data.BudgetSearchStateData;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BCostCenterListWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BCostCenterWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BCostCentersData;
import de.hybris.platform.b2bwebservicescommons.dto.mycompany.BudgetListWsDTO;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.webservicescommons.errors.exceptions.NotFoundException;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class B2BCostCentersHelper extends AbstractHelper
{

	private static final Logger LOG = LoggerFactory.getLogger(B2BCostCentersHelper.class);

	private static final String COST_CENTER_NOT_FOUND = "Cost center with code [%s] was not found";
	private static final String UNIT_NOT_FOUND = "Unit with code [%s} was not found";
	private static final String BUDGET_NOT_FOUND = "Budget with uid [%s] was not found";

	@Resource(name = "costCenterFacade")
	private B2BCostCenterFacade costCenterFacade;

	@Resource(name = "budgetFacade")
	private B2BBudgetFacade budgetFacade;

	@Resource(name = "b2bUnitFacade")
	private B2BUnitFacade b2BUnitFacade;

	public B2BCostCenterListWsDTO searchCostCenters(final int currentPage, final int pageSize, final String sort,
			final String fields)
	{
		final PageableData pageableData = createPageableData(currentPage, pageSize, sort);
		final SearchPageData<B2BCostCenterData> searchPageData = costCenterFacade.search(null, pageableData);
		final B2BCostCentersData b2bCostCentersData = createB2BCostCentersData(searchPageData);
		return getDataMapper().map(b2bCostCentersData, B2BCostCenterListWsDTO.class, fields);
	}

	public B2BCostCenterListWsDTO searchActiveCostCenters(final String fields)
	{
		final List<? extends B2BCostCenterData> costCenterData = costCenterFacade.getActiveCostCenters();
		final B2BCostCenterListWsDTO b2BCostCenterListWsDTO = new B2BCostCenterListWsDTO();
		b2BCostCenterListWsDTO.setCostCenters(getDataMapper().mapAsList(costCenterData, B2BCostCenterWsDTO.class, fields));
		return b2BCostCenterListWsDTO;
	}

	public B2BCostCenterWsDTO searchCostCenter(final String costCenterCode, final String fields)
	{
		final B2BCostCenterData b2BCostCenterData = getCostCenterDataForCode(costCenterCode);
		return getDataMapper().map(b2BCostCenterData, B2BCostCenterWsDTO.class, fields);
	}

	public void addCostCenter(final B2BCostCenterWsDTO costCenter)
	{
		final B2BCostCenterData b2BCostCenterData = getDataMapper().map(costCenter, B2BCostCenterData.class);
		final B2BUnitData b2BUnitData = getB2BUnitDataByUid(costCenter.getUnit().getUid());
		b2BCostCenterData.setUnit(b2BUnitData);
		costCenterFacade.addCostCenter(b2BCostCenterData);
	}

	public void enableDisableCostCenter(final String costCenterCode, final B2BCostCenterWsDTO costCenter)
	{
		if (costCenter.getActiveFlag() != null)
		{
			costCenterFacade.enableDisableCostCenter(costCenterCode, costCenter.getActiveFlag());
			costCenter.setActiveFlag(null);
		}
	}

	public B2BSelectionData addBudgetToCostCenter(String costCenterCode, String budgetCode)
	{
		checkIfCostCenterAndBudgetExist(costCenterCode, budgetCode);
		return costCenterFacade.selectBudgetForCostCenter(costCenterCode, budgetCode);
	}

	public B2BSelectionData removeBudgetFromCostCenter(String costCenterCode, String budgetCode)
	{
		checkIfCostCenterAndBudgetExist(costCenterCode, budgetCode);
		return costCenterFacade.deSelectBudgetForCostCenter(costCenterCode, budgetCode);
	}

	public BudgetListWsDTO searchBudgetsForCostCenter(final String costCenterCode, final int currentPage, final int pageSize,
			final String sort, final String fields)
	{
		final BudgetSearchStateData searchState = getBudgetsSearchState(costCenterCode);
		final PageableData pageableData = createPageableData(currentPage, pageSize, sort);
		final SearchPageData<B2BBudgetData> searchPageData = budgetFacade.search(searchState, pageableData);
		final BudgetsData budgetsData = createBudgetsData(searchPageData);
		return getDataMapper().map(budgetsData, BudgetListWsDTO.class, fields);
	}

	public B2BCostCenterData getCostCenterDataForCode(final String costCenterCode)
	{
		try
		{
			return costCenterFacade.getCostCenterDataForCode(costCenterCode);
		}
		catch (IllegalArgumentException e)
		{
			final String message = String.format(COST_CENTER_NOT_FOUND, sanitize(costCenterCode));
			LOG.error(message, e);
			throw new NotFoundException(message);
		}
	}

	private B2BUnitData getB2BUnitDataByUid(final String unitUid)
	{
		final B2BUnitData b2BUnitData = b2BUnitFacade.getUnitForUid(unitUid);
		if (b2BUnitData == null)
		{
			final String message = String.format(UNIT_NOT_FOUND, sanitize(unitUid));
			LOG.error(message);
			throw new NotFoundException(message);
		}
		return b2BUnitData;
	}

	private B2BCostCentersData createB2BCostCentersData(final SearchPageData<B2BCostCenterData> searchPageData)
	{
		final B2BCostCentersData b2BCostCentersData = new B2BCostCentersData();
		b2BCostCentersData.setCostCenters(searchPageData.getResults());
		b2BCostCentersData.setSorts(searchPageData.getSorts());
		b2BCostCentersData.setPagination(searchPageData.getPagination());
		return b2BCostCentersData;
	}

	private void checkIfCostCenterAndBudgetExist(final String costCenterCode, final String budgetCode)
	{
		getCostCenterDataForCode(costCenterCode);
		if (budgetFacade.getBudgetDataForCode(budgetCode) == null)
		{
			final String message = String.format(BUDGET_NOT_FOUND, sanitize(budgetCode));
			LOG.error(message);
			throw new NotFoundException(message);
		}
	}

	private BudgetSearchStateData getBudgetsSearchState(final String costCenterCode)
	{
		// To check if the cost center exists
		getCostCenterDataForCode(costCenterCode);

		final BudgetSearchStateData searchState = new BudgetSearchStateData();
		searchState.setCostCenterCode(costCenterCode);
		return searchState;
	}

	private BudgetsData createBudgetsData(final SearchPageData<B2BBudgetData> searchPageData)
	{
		final BudgetsData budgetsData = new BudgetsData();
		budgetsData.setBudgets(searchPageData.getResults());
		budgetsData.setSorts(searchPageData.getSorts());
		budgetsData.setPagination(searchPageData.getPagination());
		return budgetsData;
	}
}
