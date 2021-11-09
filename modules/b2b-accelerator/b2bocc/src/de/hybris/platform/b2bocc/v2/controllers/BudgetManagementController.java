/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bocc.v2.controllers;

import de.hybris.platform.b2b.model.B2BBudgetModel;
import de.hybris.platform.b2bocc.security.SecuredAccessConstants;
import de.hybris.platform.b2bocc.v2.helper.BudgetManagementHelper;
import de.hybris.platform.b2bcommercefacades.company.B2BBudgetFacade;
import de.hybris.platform.b2bcommercefacades.company.data.B2BBudgetData;
import de.hybris.platform.b2bwebservicescommons.dto.mycompany.BudgetListWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.mycompany.BudgetWsDTO;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.webservicescommons.errors.exceptions.AlreadyExistsException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.webservicescommons.errors.exceptions.NotFoundException;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
@RequestMapping(value = "/{baseSiteId}/users/{userId}/budgets")
@ApiVersion("v2")
@Api(tags = "Budget Management")
public class BudgetManagementController extends BaseController
{
    private static final Logger LOG = LoggerFactory.getLogger(BudgetManagementController.class);

    private static final String OBJECT_NAME_BUDGET = "budget";
    private static final String BUDGET_CODE_PATH_VARIABLE_PATTERN = "/{budgetCode:.*}";
	private static final String BUDGET_NOT_FOUND_ERROR_KEY = "error.bugetcode.notfound";
	private static final String BUDGET_ALREADY_EXISTS_MESSAGE = "Budget with code [%s] already exists";
	private static final String MODEL_SAVING_ERROR_MESSAGE = "Model saving error.";

	@Resource(name = "budgetFacade")
	protected B2BBudgetFacade budgetFacade;
	@Resource(name = "budgetManagementHelper")
	private BudgetManagementHelper budgetManagementHelper;
	@Resource(name = "budgetManagementValidator")
	private Validator budgetManagementValidator;
	@Resource(name = "budgetWsDTOValidator")
	private Validator budgetWsDTOValidator;

	protected static final String DEFAULT_PAGE_SIZE = "20";
	protected static final String DEFAULT_CURRENT_PAGE = "0";

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@ApiOperation(nickname = "getBudgets", value = "Gets the list of budgets for a specified base store", notes = "Returns the list of budgets accessible to a specified user for a specified base store. The response can display the results across multiple pages, if required.", produces = MediaType.APPLICATION_JSON)
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	@ApiBaseSiteIdAndUserIdParam
	public BudgetListWsDTO getBudgets(
			@ApiParam(value = "The current result page requested.", required = false) @RequestParam(value = "currentPage", defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@ApiParam(value = "The number of results returned per page.", required = false) @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@ApiParam(value = "Sorting method applied to the returned results.", required = false) @RequestParam(value = "sort", defaultValue = B2BBudgetModel.CODE) final String sort,
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{
		final BudgetListWsDTO budgetList = budgetManagementHelper
				.searchBudget(currentPage, pageSize, sort, addPaginationField(fields), null);

		return budgetList;
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@ApiOperation(nickname = "getBudget", value = "Gets specific budget details accessible to a specified user for a specified base store based on budget code", notes = "Returns specific budget details accessible to a specified user for a specified base store based on budget code. The response contains detailed order information.", produces = MediaType.APPLICATION_JSON)
	@ResponseBody
	@RequestMapping(value = BUDGET_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	@ApiBaseSiteIdAndUserIdParam
	public BudgetWsDTO getBudget(
			@ApiParam(value = "Budget GUID (Globally Unique Identifier) or budget CODE.", required = true) @PathVariable final String budgetCode,
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{
		B2BBudgetData budgetData = budgetFacade.getBudgetDataForCode(budgetCode);

		if (budgetData == null)
		{
			throw new NotFoundException(BUDGET_NOT_FOUND_ERROR_KEY);
		}

		return getDataMapper().map(budgetData, BudgetWsDTO.class, fields);
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@ApiOperation(nickname = "updateBudget", value = "Updates the budget", notes = "Updates the budget. Only attributes provided in the request body will be changed.", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = BUDGET_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.PATCH)
	@ApiBaseSiteIdAndUserIdParam
	public BudgetWsDTO updateBudget(@ApiParam(value = "Budget.", required = true) @RequestBody final BudgetWsDTO budget,
			@ApiParam(value = "Budget GUID (Globally Unique Identifier) or budget CODE.", required = true) @PathVariable final String budgetCode,
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{
		B2BBudgetData budgetData = budgetFacade.getBudgetDataForCode(budgetCode);

		validate(budgetData, "budgetData", budgetManagementValidator);

		if (budget.getActive() != null)
		{
			budgetFacade.enableDisableBudget(budgetCode, budget.getActive());
			budget.setActive(null);
		}

		getDataMapper().map(budget, budgetData, false);
		budgetData.setOriginalCode(budgetCode);

		final BudgetWsDTO toBeValidatedBudget = getDataMapper().map(budgetData, BudgetWsDTO.class);
		validate(toBeValidatedBudget, OBJECT_NAME_BUDGET, budgetWsDTOValidator);

		budgetFacade.updateBudget(budgetData);

		budgetData = budgetFacade.getBudgetDataForCode(budgetData.getCode());

		return getDataMapper().map(budgetData, BudgetWsDTO.class, fields);
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@ApiOperation(nickname = "createBudget", value = " Creates a new budget", notes = "Creates a new budget.", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(method = RequestMethod.POST)
	@ApiBaseSiteIdAndUserIdParam
	public BudgetWsDTO createBudget(@ApiParam(value = "Budget", required = true) @RequestBody final BudgetWsDTO budget,
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{
		validate(budget, OBJECT_NAME_BUDGET, budgetWsDTOValidator);
		if (budgetFacade.getBudgetDataForCode(budget.getCode()) != null)
		{
			throw new AlreadyExistsException(String.format(BUDGET_ALREADY_EXISTS_MESSAGE, budget.getCode()));
		}

		Boolean active = budget.getActive();
		budget.setActive(null);

		final B2BBudgetData budgetData = getDataMapper().map(budget, B2BBudgetData.class);
		budgetFacade.addBudget(budgetData);

		if (active != null)
		{
			budgetFacade.enableDisableBudget(budgetData.getCode(), active);
		}

		final B2BBudgetData updatedBudgetData = budgetFacade.getBudgetDataForCode(budgetData.getCode());
		return getDataMapper().map(updatedBudgetData, BudgetWsDTO.class, fields);
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	@ExceptionHandler({ ModelSavingException.class })
	public ErrorListWsDTO handleModelSavingException(final Exception ex)
	{
		LOG.debug("ModelSavingException", ex);
		return handleErrorInternal(ModelSavingException.class.getSimpleName(), MODEL_SAVING_ERROR_MESSAGE);
	}
}
