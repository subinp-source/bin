/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bocc.v2.controllers;

import de.hybris.platform.b2bocc.security.SecuredAccessConstants;
import de.hybris.platform.b2bacceleratorfacades.api.cart.CartFacade;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BPaymentTypeData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BCostCenterData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.CartModificationDataList;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.commerceservices.request.mapping.annotation.RequestMappingOverride;
import de.hybris.platform.commercewebservicescommons.annotation.SiteChannelRestriction;
import de.hybris.platform.commercewebservicescommons.dto.order.CartModificationListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.CartModificationWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.CartWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryWsDTO;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdUserIdAndCartIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
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

import static de.hybris.platform.b2bocc.constants.B2boccConstants.OCC_REWRITE_OVERLAPPING_BASE_SITE_USER_PATH;


@Controller
@ApiVersion("v2")
@Api(tags = "B2B Carts")
public class B2BCartsController extends BaseController
{
	protected static final String API_COMPATIBILITY_B2B_CHANNELS = "api.compatibility.b2b.channels";

	@Resource(name = "b2bCartFacade")
	private CartFacade cartFacade;

	@Resource(name = "userFacade")
	protected UserFacade userFacade;

	@Resource(name = "dataMapper")
	protected DataMapper dataMapper;

	@Resource(name = "b2BCartAddressValidator")
	private Validator b2BCartAddressValidator;

	@ApiOperation(nickname = "addOrgCartEntry", hidden = true, value = "Adds more quantity to the cart for a specific entry",
			notes = "Adds more quantity to the cart for a specific entry based on it's product code, if the product is already in the cart the amount will be added to the existing quantity.")
	@RequestMappingOverride(priorityProperty = "b2bocc.CartResource.addCartEntry.priority")
	@SiteChannelRestriction(allowedSiteChannelsProperty = API_COMPATIBILITY_B2B_CHANNELS)
	@RequestMapping(value = OCC_REWRITE_OVERLAPPING_BASE_SITE_USER_PATH + "/carts/{cartId}/entries", method = RequestMethod.POST)
	@ResponseBody
	@ApiBaseSiteIdUserIdAndCartIdParam
	public CartModificationWsDTO addCartEntry(
			@ApiParam(value = "Base site identifier.", required = true) @PathVariable final String baseSiteId,
			@ApiParam(value = "Code of the product to be added to the cart.", required = true) @RequestParam(required = true) final String code,
			@ApiParam(value = "Amount to be added.", required = false) @RequestParam(required = false, defaultValue = "1") final long quantity,
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{
		final OrderEntryData orderEntry = getOrderEntryData(quantity, code, null);

		return dataMapper.map(cartFacade.addOrderEntry(orderEntry), CartModificationWsDTO.class, fields);
	}

	@ApiOperation(nickname = "updateOrgCartEntry", hidden = true, value = "Updates the total amount of a specific product",
			notes = "Updates the total amount of a specific product in the cart based on the entryNumber.")
	@RequestMappingOverride(priorityProperty = "b2bocc.CartResource.updateCartEntry.priority")
	@SiteChannelRestriction(allowedSiteChannelsProperty = API_COMPATIBILITY_B2B_CHANNELS)
	@RequestMapping(value = OCC_REWRITE_OVERLAPPING_BASE_SITE_USER_PATH
			+ "/carts/{cartId}/entries/{entryNumber}", method = RequestMethod.PUT)
	@ResponseBody
	@ApiBaseSiteIdUserIdAndCartIdParam
	public CartModificationWsDTO updateCartEntry(@ApiParam(value = "Base site identifier.") @PathVariable final String baseSiteId,
			@ApiParam(value = "The id of the entry in the cart.") @PathVariable final int entryNumber,
			@ApiParam(value = "New quantity for this entry.") @RequestParam(required = true) final Long quantity,
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{
		return updateCartEntry(baseSiteId, null, entryNumber, quantity, fields);
	}

	@ApiOperation(nickname = "updateOrgCartProductEntry", hidden = true, value = "Updates the total amount of a specific product"
			, notes = "Updates the total amount of a specific product in the cart based either in the product code or the entryNumber.")
	@RequestMappingOverride(priorityProperty = "b2bocc.CartResource.updateCartEntryByProduct.priority")
	@SiteChannelRestriction(allowedSiteChannelsProperty = API_COMPATIBILITY_B2B_CHANNELS)
	@RequestMapping(value = OCC_REWRITE_OVERLAPPING_BASE_SITE_USER_PATH + "/carts/{cartId}/entries/", method = RequestMethod.PUT)
	@ResponseBody
	@ApiBaseSiteIdUserIdAndCartIdParam
	public CartModificationWsDTO updateCartEntry(@ApiParam(value = "Base site identifier.") @PathVariable final String baseSiteId,
			@ApiParam(value = "Code of the product to be added to the cart, this code is not considered if an entryNumber is passed.") @RequestParam(required = false) final String product,
			@ApiParam(value = "The id of the entry in the cart, this parameter takes precedence over the product code.") @RequestParam(required = false) final Integer entryNumber,
			@ApiParam(value = "New quantity for this product.") @RequestParam(required = true) final Long quantity,
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{
		final OrderEntryData orderEntry = getOrderEntryData(quantity, product, entryNumber);

		return dataMapper.map(cartFacade.updateOrderEntry(orderEntry), CartModificationWsDTO.class, fields);
	}

	@ApiOperation(nickname = "doAddOrgCartEntries", value = "Adds more quantity to the cart of specific products", notes = "Updates the details of specified products in the cart, based either on the product code or the entryNumber.")
	@RequestMappingOverride(priorityProperty = "b2bocc.CartResource.addCartEntries.priority")
	@SiteChannelRestriction(allowedSiteChannelsProperty = API_COMPATIBILITY_B2B_CHANNELS)
	@RequestMapping(value = OCC_REWRITE_OVERLAPPING_BASE_SITE_USER_PATH
			+ "/carts/{cartId}/entries/", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@ApiBaseSiteIdUserIdAndCartIdParam
	public CartModificationListWsDTO addCartEntries(
			@ApiParam(value = "Base site identifier.") @PathVariable final String baseSiteId,
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields,
			@ApiParam(value = "List of entries containing the amount to add and the product code or the entryNumber.") @RequestBody(required = true) final OrderEntryListWsDTO entries)
	{
		final List<OrderEntryData> cartEntriesData = convertToData(entries);
		final List<CartModificationData> resultList = cartFacade.addOrderEntryList(cartEntriesData);

		return dataMapper.map(getCartModificationDataList(resultList), CartModificationListWsDTO.class, fields);

	}

	@ApiOperation(nickname = "replaceOrgCartEntries", value = "Updates the quantity for specific products in the cart", notes = "Creates the specified products in the cart, or overwrites the details of existing products in the cart, based either on the product code or the entryNumber. For existing products, attributes not provided in the request body will be defined again (set to null or default).")
	@RequestMappingOverride(priorityProperty = "b2bocc.CartResource.updateCartEntries.priority")
	@SiteChannelRestriction(allowedSiteChannelsProperty = API_COMPATIBILITY_B2B_CHANNELS)
	@RequestMapping(value = OCC_REWRITE_OVERLAPPING_BASE_SITE_USER_PATH
			+ "/carts/{cartId}/entries/", method = RequestMethod.PUT, consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@ApiBaseSiteIdUserIdAndCartIdParam
	public CartModificationListWsDTO updateCartEntries(
			@ApiParam(value = "Base site identifier.") @PathVariable final String baseSiteId,
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields,
			@ApiParam(value = "List of entries containing the amount to add and the product code or the entryNumber.") @RequestBody(required = true) final OrderEntryListWsDTO entries)
	{
		final List<OrderEntryData> cartEntriesData = convertToData(entries);
		final List<CartModificationData> resultList = cartFacade.updateOrderEntryList(cartEntriesData);

		return dataMapper.map(getCartModificationDataList(resultList), CartModificationListWsDTO.class, fields);
	}

	@ApiOperation(nickname = "replaceOrgCartCostCenter", value = "Sets the cost center for the checkout cart.", notes = "Sets the cost center for the checkout cart.")
	@Secured({ SecuredAccessConstants.ROLE_CUSTOMERGROUP, SecuredAccessConstants.ROLE_GUEST,
			SecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/{baseSiteId}/users/{userId}/carts/{cartId}/costcenter", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiBaseSiteIdUserIdAndCartIdParam
	public CartWsDTO setCartCostCenter(
			@ApiParam(value = "The id of the cost center.", required = true) @RequestParam(required = true) final String costCenterId,
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{
		final B2BCostCenterData costCenter = new B2BCostCenterData();
		costCenter.setCode(costCenterId);

		CartData cartData = new CartData();
		cartData.setCostCenter(costCenter);

		cartData = cartFacade.update(cartData);

		return dataMapper.map(cartData, CartWsDTO.class, fields);
	}

	@ApiOperation(nickname = "replaceOrgCartPaymentType", value = "Set the payment type for the checkout cart", notes = "Sets the payment type for the checkout cart. If the purchaseOrderNumber is not null, the purchaseOrderNumber is also assigned to the cart.")
	@Secured({ SecuredAccessConstants.ROLE_CUSTOMERGROUP, SecuredAccessConstants.ROLE_GUEST,
			SecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/{baseSiteId}/users/{userId}/carts/{cartId}/paymenttype", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiBaseSiteIdUserIdAndCartIdParam
	public CartWsDTO setPaymentType(
			@ApiParam(value = "Payment type choice (between card and account).", required = true) @RequestParam(required = true) final String paymentType,
			@ApiParam(value = "Purchase order number to assign to the checkout cart.") @RequestParam(required = false) final String purchaseOrderNumber,
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{
		final B2BPaymentTypeData paymentTypeData = new B2BPaymentTypeData();
		paymentTypeData.setCode(paymentType);

		CartData cartData = new CartData();
		cartData.setPaymentType(paymentTypeData);

		if (purchaseOrderNumber != null)
		{
			cartData.setPurchaseOrderNumber(purchaseOrderNumber);
		}

		cartData = cartFacade.update(cartData);

		return dataMapper.map(cartData, CartWsDTO.class, fields);
	}

	@ApiOperation(nickname = "replaceOrgCartDeliveryAddress", value = "Sets the delivery address for the checkout cart.", notes = "Sets the delivery address for the checkout cart.")
	@Secured({ SecuredAccessConstants.ROLE_CUSTOMERGROUP, SecuredAccessConstants.ROLE_GUEST,
			SecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = OCC_REWRITE_OVERLAPPING_BASE_SITE_USER_PATH
			+ "/carts/{cartId}/addresses/delivery", method = RequestMethod.PUT)
	@RequestMappingOverride(priorityProperty = "b2bocc.B2BOrdersController.setCartDeliveryAddress.priority")
	@SiteChannelRestriction(allowedSiteChannelsProperty = API_COMPATIBILITY_B2B_CHANNELS)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiBaseSiteIdUserIdAndCartIdParam
	public CartWsDTO setCartDeliveryAddress(
			@ApiParam(value = "The id of the address.", required = true) @RequestParam(required = true) final String addressId,
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{
		validateCartAddress(addressId);
		final AddressData address = new AddressData();
		address.setId(addressId);

		CartData cartData = new CartData();
		cartData.setDeliveryAddress(address);

		cartData = cartFacade.update(cartData);

		return dataMapper.map(cartData, CartWsDTO.class, fields);
	}

	@ApiOperation(nickname = "getCurrentOrgCart", value = "Gets the current cart.", notes = "Gets the current cart.")
	@RequestMapping(value = "/{baseSiteId}/users/{userId}/carts/current", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiBaseSiteIdAndUserIdParam
	public CartWsDTO getCurrentCart(
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{
		if (userFacade.isAnonymousUser())
		{
			throw new AccessDeniedException("Access is denied");
		}

		return dataMapper.map(cartFacade.getCurrentCart(), CartWsDTO.class, fields);
	}

	protected CartModificationDataList getCartModificationDataList(final List<CartModificationData> result)
	{
		final CartModificationDataList cartModificationDataList = new CartModificationDataList();
		cartModificationDataList.setCartModificationList(result);
		return cartModificationDataList;
	}

	//TODO: Do this mapping automatically or put in a populator.
	protected List<OrderEntryData> convertToData(final OrderEntryListWsDTO entriesWS)
	{
		final List<OrderEntryData> entriesData = new ArrayList<>();

		for (final OrderEntryWsDTO entryDto : entriesWS.getOrderEntries())
		{
			final OrderEntryData entryData = getOrderEntryData(entryDto.getQuantity(), entryDto.getProduct().getCode(),
					entryDto.getEntryNumber());
			entriesData.add(entryData);
		}

		return entriesData;
	}

	protected OrderEntryData getOrderEntryData(final long quantity, final String productCode, final Integer entryNumber)
	{
		final OrderEntryData orderEntry = new OrderEntryData();
		orderEntry.setQuantity(quantity);
		orderEntry.setProduct(new ProductData());
		orderEntry.getProduct().setCode(productCode);
		orderEntry.setEntryNumber(entryNumber);

		return orderEntry;
	}

	protected void validateCartAddress(final String addressId)
	{
		final Errors errors = new BeanPropertyBindingResult(addressId, "address Id");
		b2BCartAddressValidator.validate(addressId, errors);
		if (errors.hasErrors())
		{
			throw new WebserviceValidationException(errors);
		}
	}
}