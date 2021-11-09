/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.occ.v2.controllers;

import de.hybris.platform.b2b.occ.security.SecuredAccessConstants;
import de.hybris.platform.b2b.occ.v2.helper.OrderApprovalPermissionTypesHelper;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BPermissionTypeListWsDTO;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.webservicescommons.cache.CacheControl;
import de.hybris.platform.webservicescommons.cache.CacheControlDirective;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Controller
@RequestMapping(value = "/{baseSiteId}/orderApprovalPermissionTypes")
@CacheControl(directive = CacheControlDirective.PRIVATE)
@ApiVersion("v2")
@Api(tags = "Order Approval Permission Types")
public class OrderApprovalPermissionTypesController extends BaseController
{
	@Resource(name = "orderApprovalPermissionTypesHelper")
	protected OrderApprovalPermissionTypesHelper permissionTypesHelper;

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(nickname = "getOrderApprovalPermissionTypes", value = "Get order approval permission types.", notes = "Returns the list of order approval permission types.", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiBaseSiteIdParam
	public B2BPermissionTypeListWsDTO getOrderApprovalPermissionTypes(
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{
		return permissionTypesHelper.getPermissionTypes(fields);
	}
}
