/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bocc.v2.controllers;

import de.hybris.platform.b2bocc.security.SecuredAccessConstants;
import de.hybris.platform.b2bocc.v2.helper.ReplenishmentOrderHelper;
import de.hybris.platform.b2bacceleratorservices.model.process.ReplenishmentProcessModel;
import de.hybris.platform.b2bwebservicescommons.dto.order.ReplenishmentOrderListWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.order.ReplenishmentOrderWsDTO;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderHistoryListWsDTO;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


@Controller
@RequestMapping(value = "/{baseSiteId}/users/{userId}/replenishmentOrders")
@ApiVersion("v2")
@Api(tags = "Replenishment Order")
public class ReplenishmentOrderController extends BaseController
{

	@Resource(name = "replenishmentOrderHelper")
	private ReplenishmentOrderHelper replenishmentOrderHelper;

	@Secured(
	{ SecuredAccessConstants.ROLE_CUSTOMERGROUP, SecuredAccessConstants.ROLE_GUEST,
			SecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@ResponseBody
	@ApiBaseSiteIdAndUserIdParam
	@RequestMapping(method = RequestMethod.GET)
	@ApiOperation(nickname = "getReplenishmentOrders",
            value = "Gets the list of replenishment orders for a specified user.",
            notes = "Returns the list of replenishment orders accessible to a specified user.",
            produces = MediaType.APPLICATION_JSON)
	public ReplenishmentOrderListWsDTO getReplenishmentOrders(
			@ApiParam(value = "The current result page requested.") @RequestParam(defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@ApiParam(value = "The number of results returned per page.") @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@ApiParam(value = "Sorting method applied to the returned results.") @RequestParam(defaultValue = ReplenishmentProcessModel.CODE) final String sort,
			@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		return replenishmentOrderHelper.searchReplenishments(currentPage, pageSize, sort, addPaginationField(fields));
	}

	@Secured(
	{ SecuredAccessConstants.ROLE_CUSTOMERGROUP, SecuredAccessConstants.ROLE_GUEST,
			SecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@ResponseBody
	@ApiBaseSiteIdAndUserIdParam
	@RequestMapping(value = "/{replenishmentOrderCode}", method = RequestMethod.GET)
	@ApiOperation(nickname = "getReplenishmentOrder",
            value = "Gets replenishment order for a specified user and replenishment order code.",
            notes = "Returns specific replenishment order details accessible for a specified user. The response contains detailed orders information from the replenishment order.",
            produces = MediaType.APPLICATION_JSON)
	public ReplenishmentOrderWsDTO getReplenishmentOrder(
			@ApiParam(value = "Unique code for the replenishment order.", required = true) @PathVariable final String replenishmentOrderCode,
			@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		return replenishmentOrderHelper.searchReplenishment(replenishmentOrderCode, fields);
	}

	@Secured(
	{ SecuredAccessConstants.ROLE_CUSTOMERGROUP, SecuredAccessConstants.ROLE_GUEST,
			SecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@ResponseBody
	@ApiBaseSiteIdAndUserIdParam
	@RequestMapping(value = "/{replenishmentOrderCode}", method = RequestMethod.PATCH)
	@ApiOperation(nickname = "updateReplenishmentOrder",
            value = "Updates the replenishment order for a specified user and replenishment order code.",
            notes = "Updates the replenishment order. Only cancellation of the replenishment order is supported by setting the attribute 'active' to FALSE. Cancellation of the replenishment order cannot be reverted.",
            produces = MediaType.APPLICATION_JSON)
	public ReplenishmentOrderWsDTO updateReplenishmentOrder(
			@ApiParam(value = "Unique code for the replenishment order.", required = true) @PathVariable final String replenishmentOrderCode,
			@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		return replenishmentOrderHelper.cancelReplenishment(replenishmentOrderCode, fields);
	}

	@Secured(
	{ SecuredAccessConstants.ROLE_CUSTOMERGROUP, SecuredAccessConstants.ROLE_GUEST,
			SecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@ResponseBody
	@ApiBaseSiteIdAndUserIdParam
	@RequestMapping(value = "/{replenishmentOrderCode}/orders", method = RequestMethod.GET)
	@ApiOperation(nickname = "getReplenishmentOrderHistory",
            value = "Gets replenishment order history.",
            notes = "Returns order history data from a replenishment order placed by a specified user.",
            produces = MediaType.APPLICATION_JSON)
	public OrderHistoryListWsDTO getReplenishmentOrderHistory(
			@ApiParam(value = "The current result page requested.") @RequestParam(defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@ApiParam(value = "The number of results returned per page.") @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@ApiParam(value = "Sorting method applied to the returned results.") @RequestParam(defaultValue = ReplenishmentProcessModel.CODE) final String sort,
			@ApiParam(value = "Unique code for the replenishment order.", required = true) @PathVariable final String replenishmentOrderCode,
			@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		return replenishmentOrderHelper.searchOrderHistories(replenishmentOrderCode, currentPage, pageSize, sort,
				addPaginationField(fields));
	}

}
