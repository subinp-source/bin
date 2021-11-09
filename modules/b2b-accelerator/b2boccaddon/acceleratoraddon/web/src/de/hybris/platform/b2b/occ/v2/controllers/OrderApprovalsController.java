/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.occ.v2.controllers;

import de.hybris.platform.b2b.occ.security.SecuredAccessConstants;
import de.hybris.platform.b2b.occ.v2.helper.OrderApprovalsHelper;
import de.hybris.platform.b2bacceleratorfacades.exception.PrincipalAssignedValidationException;
import de.hybris.platform.b2bacceleratorfacades.order.B2BOrderFacade;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BOrderApprovalData;
import de.hybris.platform.b2bwebservicescommons.dto.order.OrderApprovalDecisionWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.order.OrderApprovalListWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.order.OrderApprovalWsDTO;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.webservicescommons.errors.exceptions.NotFoundException;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;
import de.hybris.platform.workflow.exceptions.WorkflowActionDecideException;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequestMapping(value = "/{baseSiteId}/users/{userId}/orderapprovals")
@ApiVersion("v2")
@Api(tags = "Order Approvals")
public class OrderApprovalsController extends BaseController
{
	protected static final String DEFAULT_PAGE_SIZE = "20";
	protected static final String DEFAULT_CURRENT_PAGE = "0";

	protected static final String RESOURCE_NOT_FOUND_ERROR_MESSAGE = "Requested resource cannot be found.";
	protected static final String ILLEGAL_ARGUMENT_ERROR_MESSAGE = "Illegal argument error.";
	protected static final String WORKFLOW_ACTION_DECIDE_ERROR_MESSAGE = "An error occurred during the execution of the approval workflow.";

	private static final Logger LOG = Logger.getLogger(OrderApprovalsController.class);

	@Resource(name = "b2bOrderFacade")
	private B2BOrderFacade orderFacade;

	@Resource(name = "orderApprovalsHelper")
	protected OrderApprovalsHelper orderApprovalsHelper;

	@Resource(name = "orderApprovalDecisionValidator")
	private Validator orderApprovalDecisionValidator;

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_B2BAPPROVERGROUP,
			SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(nickname = "getOrderApprovals", value = "Get orders to approve.", notes = "Returns the list of orders the specified user is allowed to approve.", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiBaseSiteIdAndUserIdParam
	public OrderApprovalListWsDTO getOrderApprovals(
			@ApiParam(value = "The current result page requested.") @RequestParam(defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@ApiParam(value = "The number of results returned per page.") @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@ApiParam(value = "Sorting method applied to the return results.") @RequestParam(required = false) final String sort,
			@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		return orderApprovalsHelper.searchApprovals(currentPage, pageSize, sort, fields);
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_B2BAPPROVERGROUP,
			SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/{orderApprovalCode}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(nickname = "getOrderApproval", value = "Get an order to approve.", notes = "Returns specific order details based on a specific order code. The response contains detailed order information.", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiBaseSiteIdAndUserIdParam
	public OrderApprovalWsDTO getOrderApproval(
			@ApiParam(value = "Code that identifies the order approval.", required = true) @PathVariable final String orderApprovalCode,
			@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final B2BOrderApprovalData orderApprovalDetails = orderFacade.getOrderApprovalDetailsForCode(orderApprovalCode);
		return getDataMapper().map(orderApprovalDetails, OrderApprovalWsDTO.class, fields);
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_B2BAPPROVERGROUP,
			SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/{orderApprovalCode}/decision", method = RequestMethod.POST)
	@ApiOperation(nickname = "doMakeOrderApprovalDecision", value = "Makes an approval decision for an order.", notes = "Makes a decision on the order approval that will trigger the next step in the approval workflow.", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiBaseSiteIdAndUserIdParam
	public OrderApprovalDecisionWsDTO orderApprovalDecision(
			@ApiParam(value = "Code that identifies the order approval.", required = true) @PathVariable final String orderApprovalCode,
			@ApiParam(value = "The order approval decision. The approval decision field is mandatory, and the approval comment field is mandatory if the decision is 'rejected'.", required = true) @RequestBody final OrderApprovalDecisionWsDTO orderApprovalDecision,
			@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		validate(orderApprovalDecision, "orderApproval", orderApprovalDecisionValidator);

		final B2BOrderApprovalData b2bOrderApprovalData = getDataMapper().map(orderApprovalDecision, B2BOrderApprovalData.class);
		b2bOrderApprovalData.setWorkflowActionModelCode(orderApprovalCode);

		orderFacade.setOrderApprovalDecision(b2bOrderApprovalData);

		return getDataMapper().map(b2bOrderApprovalData, OrderApprovalDecisionWsDTO.class, fields);
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	@ExceptionHandler({ IllegalArgumentException.class })
	public ErrorListWsDTO handleIllegalArgumentException(final Exception ex)
	{
		LOG.debug("IllegalArgumentException", ex);
		return handleErrorInternal(IllegalArgumentException.class.getSimpleName(), ILLEGAL_ARGUMENT_ERROR_MESSAGE);
	}

	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody
	@ExceptionHandler({ NullPointerException.class, PrincipalAssignedValidationException.class })
	public ErrorListWsDTO handleNotFoundExceptions(final Exception ex)
	{
		LOG.debug("Internal error", ex);
		return handleErrorInternal(NotFoundException.class.getSimpleName(), RESOURCE_NOT_FOUND_ERROR_MESSAGE);
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	@ExceptionHandler({ WorkflowActionDecideException.class })
	public ErrorListWsDTO handleWorkflowActionDecideException(final Exception ex)
	{
		LOG.debug("WorkflowActionDecideException", ex);
		return handleErrorInternal(WorkflowActionDecideException.class.getSimpleName(), WORKFLOW_ACTION_DECIDE_ERROR_MESSAGE);
	}
}
