/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.occ.v2.controllers;

import de.hybris.platform.b2b.occ.security.SecuredAccessConstants;
import de.hybris.platform.b2bacceleratorfacades.api.cart.CheckoutFacade;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BPaymentTypeData;
import de.hybris.platform.b2bwebservicescommons.dto.order.B2BPaymentTypeListWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.order.B2BPaymentTypeWsDTO;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;

import javax.annotation.Resource;

import java.util.List;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Controller
@ApiVersion("v2")
@Api(tags = "B2B Miscs")
public class B2BMiscsController extends BaseController
{
	@Resource(name = "b2bCheckoutFacade")
	private CheckoutFacade checkoutFacade;

	@Resource(name = "dataMapper")
	private DataMapper dataMapper;

	@Secured({ SecuredAccessConstants.ROLE_CUSTOMERGROUP, SecuredAccessConstants.ROLE_GUEST,
			SecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/{baseSiteId}/paymenttypes", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(nickname = "getPaymentTypes", value = "Returns a list of the available payment types.", notes = "Returns a list of the available payment types in the B2B checkout process.")
	@ApiBaseSiteIdParam
	public B2BPaymentTypeListWsDTO getPaymentTypes(
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{
		final List<? extends B2BPaymentTypeData> paymentTypeDatas = checkoutFacade.getPaymentTypes();

		final B2BPaymentTypeListWsDTO dto = new B2BPaymentTypeListWsDTO();
		dto.setPaymentTypes(dataMapper.mapAsList(paymentTypeDatas, B2BPaymentTypeWsDTO.class, fields));

		return dto;
	}
}
