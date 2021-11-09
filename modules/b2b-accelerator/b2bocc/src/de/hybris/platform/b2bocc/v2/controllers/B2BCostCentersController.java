/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bocc.v2.controllers;

import de.hybris.platform.b2bocc.security.SecuredAccessConstants;
import de.hybris.platform.b2bocc.v2.helper.B2BCostCentersHelper;
import de.hybris.platform.b2bcommercefacades.company.B2BCostCenterFacade;
import de.hybris.platform.b2bcommercefacades.company.data.B2BCostCenterData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BSelectionData;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BCostCenterListWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BCostCenterWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BSelectionDataWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.mycompany.BudgetListWsDTO;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


@Controller
@RequestMapping(value = "/{baseSiteId}")
@ApiVersion("v2")
@Api(tags = "B2B Cost Centers")
public class B2BCostCentersController extends BaseController
{

	private static final String OBJECT_NAME_COST_CENTER = "costCenter";

	@Resource(name = "b2BCostCentersHelper")
	private B2BCostCentersHelper b2BCostCentersHelper;

	@Resource(name = "costCenterFacade")
	private B2BCostCenterFacade costCenterFacade;

	@Resource(name = "b2BCostCenterWsDTOValidator")
	private Validator b2BCostCenterWsDTOValidator;

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/costcentersall", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(nickname = "getCostCenters", value = "Get cost centers.", notes = "Returns the list of all cost centers.", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiBaseSiteIdParam
	public B2BCostCenterListWsDTO getCostCenters(
			@ApiParam(value = "The current result page requested.") @RequestParam(defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@ApiParam(value = "The number of results returned per page.") @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@ApiParam(value = "Sorting method applied to the return results.") @RequestParam(required = false) final String sort,
			@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		return b2BCostCentersHelper.searchCostCenters(currentPage, pageSize, sort, addPaginationField(fields));
	}

	@Secured({ SecuredAccessConstants.ROLE_CUSTOMERGROUP, SecuredAccessConstants.ROLE_GUEST,
			SecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/costcenters", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(nickname = "getActiveCostCenters", value = "Get active cost centers.", notes = "Returns the list of all active cost centers.")
	@ApiBaseSiteIdParam
	public B2BCostCenterListWsDTO getActiveCostCenters(
			@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		return b2BCostCentersHelper.searchActiveCostCenters(fields);
	}

	@Secured({ SecuredAccessConstants.ROLE_CUSTOMERGROUP, SecuredAccessConstants.ROLE_GUEST,
			SecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/costcenters/{costCenterCode}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(nickname = "getCostCenter", value = "Get a cost center.", notes = "Returns a specific cost center based on specific code. The response contains detailed cost center information.")
	@ApiBaseSiteIdParam
	public B2BCostCenterWsDTO getCostCenter(
			@ApiParam(value = "Cost center identifier.", required = true) @PathVariable final String costCenterCode,
			@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		return b2BCostCentersHelper.searchCostCenter(costCenterCode, fields);
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/costcenters", method = RequestMethod.POST)
	@ApiOperation(nickname = "createCostCenter", value = "Create a new cost center.", notes = "Creates a new cost center.", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiBaseSiteIdParam
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public B2BCostCenterWsDTO createCostCenter(
			@ApiParam(value = "Cost center object.", required = true) @RequestBody final B2BCostCenterWsDTO costCenter,
			@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		validate(costCenter, OBJECT_NAME_COST_CENTER, b2BCostCenterWsDTOValidator);
		b2BCostCentersHelper.addCostCenter(costCenter);
		return b2BCostCentersHelper.searchCostCenter(costCenter.getCode(), fields);
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/costcenters/{costCenterCode}", method = RequestMethod.PATCH)
	@ApiOperation(nickname = "updateCostCenter", value = "Update a cost center.", notes = "Updates a cost center. Only attributes provided in the request body will be changed.", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiBaseSiteIdParam
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public B2BCostCenterWsDTO updateCostCenter(
			@ApiParam(value = "Cost center identifier.", required = true) @PathVariable final String costCenterCode,
			@ApiParam(value = "Cost center object.", required = true) @RequestBody final B2BCostCenterWsDTO costCenter,
			@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		b2BCostCentersHelper.enableDisableCostCenter(costCenterCode, costCenter);

		final B2BCostCenterData costCenterData = b2BCostCentersHelper.getCostCenterDataForCode(costCenterCode);
		getDataMapper().map(costCenter, costCenterData, false);
		costCenterData.setOriginalCode(costCenterCode);

		final B2BCostCenterWsDTO toBeValidatedCostCenter = getDataMapper().map(costCenterData, B2BCostCenterWsDTO.class);
		validate(toBeValidatedCostCenter, OBJECT_NAME_COST_CENTER, b2BCostCenterWsDTOValidator);

		costCenterFacade.updateCostCenter(costCenterData);
		return b2BCostCentersHelper.searchCostCenter(costCenterData.getCode(), fields);
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/costcenters/{costCenterCode}/budgets", method = RequestMethod.POST)
	@ApiOperation(nickname = "doAddBudgetToCostCenter", value = "Add a budget to a specific cost center.", notes = "Adds a budget to a specific cost center.", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiBaseSiteIdParam
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public B2BSelectionDataWsDTO addBudgetToCostCenter(
			@ApiParam(value = "Identifier of the cost center to which the budget will be added.", required = true) @PathVariable final String costCenterCode,
			@ApiParam(value = "The budget which will be added to a specific cost center.", required = true) @RequestParam final String budgetCode,
			@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final B2BSelectionData selectionData = b2BCostCentersHelper.addBudgetToCostCenter(costCenterCode, budgetCode);
		return getDataMapper().map(selectionData, B2BSelectionDataWsDTO.class, fields);
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/costcenters/{costCenterCode}/budgets/{budgetCode}", method = RequestMethod.DELETE)
	@ApiOperation(nickname = "removeBudgetFromCostCenter", value = "Remove a budget from a specific cost center.", notes = "Removes a budget from a specific cost center.", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiBaseSiteIdParam
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public B2BSelectionDataWsDTO removeBudgetFromCostCenter(
			@ApiParam(value = "Identifier of the cost center from which the budget will be removed.", required = true) @PathVariable final String costCenterCode,
			@ApiParam(value = "The budget which will be removed from a specific cost center.", required = true) @PathVariable final String budgetCode,
			@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final B2BSelectionData selectionData = b2BCostCentersHelper.removeBudgetFromCostCenter(costCenterCode, budgetCode);
		return getDataMapper().map(selectionData, B2BSelectionDataWsDTO.class, fields);
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/costcenters/{costCenterCode}/budgets", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(nickname = "getBudgetsForCostCenter", value = "Get all budgets and select budgets which belong to a specific cost center.", notes = "Returns the list of all budgets, where those budgets, which belong to a specific cost center, are selected.", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiBaseSiteIdParam
	public BudgetListWsDTO getBudgetsForCostCenter(
			@ApiParam(value = "Cost center identifier.", required = true) @PathVariable final String costCenterCode,
			@ApiParam(value = "The current result page requested.") @RequestParam(defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@ApiParam(value = "The number of results returned per page.") @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@ApiParam(value = "Sorting method applied to the return results.") @RequestParam(required = false) final String sort,
			@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		return b2BCostCentersHelper
				.searchBudgetsForCostCenter(costCenterCode, currentPage, pageSize, sort, addPaginationField(fields));
	}
}
