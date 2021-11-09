/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bocc.v2.controllers;

import static de.hybris.platform.b2bocc.constants.B2boccConstants.OCC_REWRITE_OVERLAPPING_BASE_SITE_USER_PATH;
import static de.hybris.platform.util.localization.Localization.getLocalizedString;

import de.hybris.platform.b2bocc.security.SecuredAccessConstants;
import de.hybris.platform.b2bacceleratorfacades.api.cart.CartFacade;
import de.hybris.platform.b2bacceleratorfacades.checkout.data.PlaceOrderData;
import de.hybris.platform.b2bacceleratorfacades.order.impl.DefaultB2BAcceleratorCheckoutFacade;
import de.hybris.platform.b2bacceleratorservices.enums.CheckoutPaymentType;
import de.hybris.platform.b2bwebservicescommons.dto.order.ReplenishmentOrderWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.order.ScheduleReplenishmentFormWsDTO;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.CartModificationDataList;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.commerceservices.request.mapping.annotation.RequestMappingOverride;
import de.hybris.platform.commercewebservicescommons.annotation.SiteChannelRestriction;
import de.hybris.platform.commercewebservicescommons.dto.order.CartModificationListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.PaymentAuthorizationException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.commercewebservicescommons.strategies.CartLoaderStrategy;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping(value = OCC_REWRITE_OVERLAPPING_BASE_SITE_USER_PATH)
@ApiVersion("v2")
@Api(tags = "B2B Orders")
public class B2BOrdersController extends BaseController
{
	protected static final String API_COMPATIBILITY_B2B_CHANNELS = "api.compatibility.b2b.channels";
	private static final String CART_CHECKOUT_TERM_UNCHECKED = "cart.term.unchecked";
	private static final String OBJECT_NAME_SCHEDULE_REPLENISHMENT_FORM = "ScheduleReplenishmentForm";

	@Resource(name = "userFacade")
	protected UserFacade userFacade;

	@Resource(name = "defaultB2BAcceleratorCheckoutFacade")
	private DefaultB2BAcceleratorCheckoutFacade b2bCheckoutFacade;

	@Resource(name = "b2bCartFacade")
	private CartFacade cartFacade;

	@Resource(name = "cartLoaderStrategy")
	private CartLoaderStrategy cartLoaderStrategy;

	@Resource(name = "dataMapper")
	private DataMapper dataMapper;

	@Resource(name = "b2BPlaceOrderCartValidator")
	private Validator placeOrderCartValidator;

	@Resource(name = "scheduleReplenishmentFormWsDTOValidator")
	private Validator scheduleReplenishmentFormWsDTOValidator;

	@Secured(
	{ SecuredAccessConstants.ROLE_CUSTOMERGROUP, SecuredAccessConstants.ROLE_GUEST,
			SecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/orders", method = RequestMethod.POST)
	@RequestMappingOverride(priorityProperty = "b2bocc.B2BOrdersController.placeOrder.priority")
	@SiteChannelRestriction(allowedSiteChannelsProperty = API_COMPATIBILITY_B2B_CHANNELS)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiBaseSiteIdAndUserIdParam
	@ApiOperation(nickname = "placeOrgOrder", value = "Places a B2B Order.", notes = "Places a B2B Order. By default the payment type is ACCOUNT. Please set payment type to CARD if placing an order using credit card.")
	public OrderWsDTO placeOrgOrder(
			@ApiParam(value = "Cart identifier: cart code for logged in user, cart guid for anonymous user, 'current' for the last modified cart", required = true) @RequestParam(required = true) final String cartId,
			@ApiParam(value = "Whether terms were accepted or not.", required = true) @RequestParam(required = true) final boolean termsChecked,
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
			throws InvalidCartException, PaymentAuthorizationException
	{

		validateTerms(termsChecked);

		validateUser();

		cartLoaderStrategy.loadCart(cartId);
		final CartData cartData = cartFacade.getCurrentCart();

		validateCart(cartData);
		validateAndAuthorizePayment(cartData);

		return dataMapper.map(b2bCheckoutFacade.placeOrder(new PlaceOrderData()), OrderWsDTO.class, fields);
	}

	@Secured(
	{ SecuredAccessConstants.ROLE_CUSTOMERGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT,
			SecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, "ROLE_CLIENT" })
	@RequestMapping(value = "/cartFromOrder", method = RequestMethod.POST)
	@SiteChannelRestriction(allowedSiteChannelsProperty = API_COMPATIBILITY_B2B_CHANNELS)
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(nickname = "createCartFromOrder", value = "Create a cart based on a previous order", notes = "Returns a list of modification applied to the new cart compared to original. e.g lower quantity was added")
	@ApiBaseSiteIdAndUserIdParam
	public CartModificationListWsDTO createCartFromOrder(
			@ApiParam(value = "The order code", required = true) @RequestParam final String orderCode,
			@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body.", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = "DEFAULT") final String fields,
			@ApiIgnore final HttpServletResponse response)
	{
		b2bCheckoutFacade.createCartFromOrder(orderCode);
		try
		{
			final List<CartModificationData> cartModifications = cartFacade.validateCurrentCartData();
			final CartModificationDataList cartModificationDataList = new CartModificationDataList();
			cartModificationDataList.setCartModificationList(cartModifications);
			response.setHeader("Location", "/current");
			return dataMapper.map(cartModificationDataList, CartModificationListWsDTO.class, fields);
		}
		catch (final CommerceCartModificationException e)
		{
			cartFacade.removeSessionCart();
			throw new IllegalArgumentException("Unable to create cart from the given order. Cart cannot be modified", e);
		}
	}

	@Secured(
	{ SecuredAccessConstants.ROLE_CUSTOMERGROUP, SecuredAccessConstants.ROLE_GUEST,
			SecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/replenishmentOrders", method = RequestMethod.POST, consumes =
	{ MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	@ApiBaseSiteIdAndUserIdParam
	@ApiOperation(nickname = "createReplenishmentOrder", value = "Creates an Order and schedules Replenishment.", notes = "Creates an Order and schedules Replenishment. By default the payment type is ACCOUNT. Please set payment type to CARD if placing an order using credit card.", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ReplenishmentOrderWsDTO createReplenishmentOrder(
			@ApiParam(value = "Cart identifier: cart code for logged in user, cart guid for anonymous user, 'current' for the last modified cart", required = true) @RequestParam(required = true) final String cartId,
			@ApiParam(value = "Whether terms were accepted or not.", required = true) @RequestParam(required = true) final boolean termsChecked,
			@ApiParam(value = "Schedule replenishment form object.", required = true) @RequestBody(required = true) final ScheduleReplenishmentFormWsDTO scheduleReplenishmentForm,
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
			throws InvalidCartException, PaymentAuthorizationException
	{

		validateTerms(termsChecked);

		validateUser();

		cartLoaderStrategy.loadCart(cartId);
		final CartData cartData = cartFacade.getCurrentCart();

		validateCart(cartData);
		validateAndAuthorizePayment(cartData);

		validateScheduleReplenishmentForm(scheduleReplenishmentForm);
		final PlaceOrderData placeOrderData = createPlaceOrderData(scheduleReplenishmentForm);

		return dataMapper.map(b2bCheckoutFacade.placeOrder(placeOrderData), ReplenishmentOrderWsDTO.class, fields);
	}

	protected void validateUser()
	{
		if (userFacade.isAnonymousUser())
		{
			throw new AccessDeniedException("Access is denied");
		}
	}

	protected void validateTerms(final boolean termsChecked)
	{
		if (!termsChecked)
		{
			throw new RequestParameterException(getLocalizedString(CART_CHECKOUT_TERM_UNCHECKED));
		}
	}

	protected void validateScheduleReplenishmentForm(ScheduleReplenishmentFormWsDTO scheduleReplenishmentForm)
	{
		validate(scheduleReplenishmentForm, OBJECT_NAME_SCHEDULE_REPLENISHMENT_FORM, scheduleReplenishmentFormWsDTOValidator);
	}

	protected void validateAndAuthorizePayment(final CartData cartData)
			throws PaymentAuthorizationException
	{
		if (CheckoutPaymentType.CARD.getCode().equals(cartData.getPaymentType().getCode()) && !b2bCheckoutFacade.authorizePayment(null))
		{
				throw new PaymentAuthorizationException();
		}
	}

	protected void validateCart(final CartData cartData)
	{
		final Errors errors = new BeanPropertyBindingResult(cartData, "sessionCart");
		placeOrderCartValidator.validate(cartData, errors);
		if (errors.hasErrors())
		{
			throw new WebserviceValidationException(errors);
		}
	}

	protected PlaceOrderData createPlaceOrderData(final ScheduleReplenishmentFormWsDTO scheduleReplenishmentForm)
	{
		final PlaceOrderData placeOrderData = new PlaceOrderData();
		dataMapper.map(scheduleReplenishmentForm, placeOrderData, false);
		if (scheduleReplenishmentForm != null)
		{
			placeOrderData.setReplenishmentOrder(Boolean.TRUE);
		}
		placeOrderData.setTermsCheck(Boolean.TRUE);
		return placeOrderData;
	}

}
