/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepaymentoccaddon.controllers;

import de.hybris.platform.chinesepaymentfacades.checkout.ChineseCheckoutFacade;
import de.hybris.platform.chinesepaymentfacades.payment.data.ChinesePaymentRequestData;
import de.hybris.platform.chinesepaymentoccaddon.exceptions.CartVoucherExpiredException;
import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.PaymentAuthorizationException;
import de.hybris.platform.chinesecommercewebservicescommons.dto.ChinesePaymentRequestWsDTO;
import de.hybris.platform.servicelayer.exceptions.BusinessException;
import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.webservicescommons.dto.error.ErrorWsDTO;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdUserIdAndCartIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * APIs for Chinese Payment.
 */
@Controller
@RequestMapping(value = "/{baseSiteId}/users/{userId}")
@Api(tags = "Payment Info")
public class PaymentInfoController
{
	@Resource(name = "chineseCheckoutFacade")
	private ChineseCheckoutFacade chineseCheckoutFacade;

	@Resource(name = "dataMapper")
	private DataMapper dataMapper;

	@Resource(name = "chinesePaymentModeCodeValidator")
	private Validator chinesePaymentModeCodeValidator;

	@Resource(name = "chineseOrderValidator")
	private Validator chineseOrderValidator;

	@Resource(name = "chinesePlaceOrderCartValidator")
	private Validator chinesePlaceOrderCartValidator;

	@Resource(name = "orderFacade")
	private OrderFacade orderFacade;

	protected static final String PAYMENT_MODE_OBJECT = "paymentModeCode";
	protected static final String ORDER_OBJECT = "order";
	protected static final String CART_OBJECT = "cartData";
	protected static final String VOUCHER_EXPIRED_MESSAGE = "The applied coupon [%s] is expired.";

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_GUEST" })
	@RequestMapping(value = "/carts/{cartId}/chinesepaymentinfo", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Updates Chinese payment info for a cart.", notes = "Updates Chinese payment info for a cart.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public void replaceChinesePaymentInfo(
			@ApiParam(value = "Payment mode code which is used to set payment provider in Chinese payment info.", required = true)
	@RequestParam
	final String paymentModeCode)
	{
		validate(paymentModeCode, PAYMENT_MODE_OBJECT, chinesePaymentModeCodeValidator);
		chineseCheckoutFacade.setPaymentInfo(paymentModeCode);
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_GUEST" })
	@RequestMapping(value = "/carts/{cartId}/createchineseorder", method = RequestMethod.POST)
	@ResponseBody()
	@ApiOperation(value = "Creates an order which is specific for China.", notes = "The check out process is different in China and it needs to create order first and not to trigger order process.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public OrderWsDTO createChineseOrder(
			@ApiFieldsParam  @RequestParam(defaultValue = "DEFAULT") final String fields)
			throws PaymentAuthorizationException, BusinessException
	{
		final CartData cart = chineseCheckoutFacade.getCheckoutCart();
		validate(cart, CART_OBJECT, chinesePlaceOrderCartValidator);
		final OrderData orderData = chineseCheckoutFacade.createOrder();
		chineseCheckoutFacade.updatePaymentInfoForPlacingOrder(orderData);
		return dataMapper.map(orderData, OrderWsDTO.class, fields);
	}

	@Secured(
	{ "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/orders/{orderCode}/payment/request", method = RequestMethod.POST)
	@ResponseBody()
	@ApiOperation(value = "Creates Chinese payment request data base on the Chinese payment info which is set in cart.", notes = "Creates Chinese payment request data base on the Chinese payment info which is set in cart.")
	@ApiBaseSiteIdAndUserIdParam
	public ChinesePaymentRequestWsDTO createChinesePaymentRequest(
			@ApiParam(value = "Order GUID (Globally Unique Identifier) or order CODE", required = true) @PathVariable("orderCode") final String orderCode,
			@ApiFieldsParam @RequestParam(defaultValue = "DEFAULT") final String fields)
	{
		final OrderData orderData = orderFacade.getOrderDetailsForCode(orderCode);
		validate(orderData, ORDER_OBJECT, chineseOrderValidator);
		final ChinesePaymentRequestData chinesePaymentRequest = chineseCheckoutFacade
				.createChinesePaymentRequestData(orderData.getCode());
		return dataMapper.map(chinesePaymentRequest, ChinesePaymentRequestWsDTO.class, fields);
	}


	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	@ExceptionHandler(
	{ CartVoucherExpiredException.class })
	public ErrorListWsDTO handleExceptions(final CartVoucherExpiredException exception)
	{
		final ErrorListWsDTO errorListDto = new ErrorListWsDTO();
		final List<ErrorWsDTO> errors = new ArrayList();

		exception.getInvalidVouchers().forEach(invalidVoucher -> {
			final ErrorWsDTO error = new ErrorWsDTO();
			error.setType(exception.getType());
			error.setSubjectType(exception.getSubjectType());
			error.setSubject(invalidVoucher);
			error.setReason(exception.getReason());
			error.setMessage(generateVoucherExpiredMessage(invalidVoucher));
			errors.add(error);
		});
		errorListDto.setErrors(errors);
		return errorListDto;
	}

	protected String generateVoucherExpiredMessage(final String voucherCode) {
		return String.format(VOUCHER_EXPIRED_MESSAGE, voucherCode);
	}

	protected void validate(final Object object, final String objectName, final Validator validator)
	{
		final Errors errors = new BeanPropertyBindingResult(object, objectName);
		validator.validate(object, errors);
		if (errors.hasErrors())
		{
			throw new WebserviceValidationException(errors);
		}
	}

}
