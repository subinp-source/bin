/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponoccaddon.controllers.customercoupon;

import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercewebservicescommons.dto.user.UserWsDTO;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.customercouponfacades.CustomerCouponFacade;
import de.hybris.platform.customercouponfacades.customercoupon.data.CustomerCouponData;
import de.hybris.platform.customercouponfacades.customercoupon.data.CustomerCouponNotificationData;
import de.hybris.platform.customercouponfacades.emums.AssignCouponResult;
import de.hybris.platform.customercouponfacades.strategies.CustomerNotificationPreferenceCheckStrategy;
import de.hybris.platform.customercouponoccaddon.dto.CustomerCoupon2CustomerWsDTO;
import de.hybris.platform.customercouponoccaddon.dto.CustomerCouponNotificationWsDTO;
import de.hybris.platform.customercouponoccaddon.dto.CustomerCouponSearchResultWsDTO;
import de.hybris.platform.customercouponoccaddon.dto.CustomerCouponWsDTO;
import de.hybris.platform.util.Config;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.pagination.WebPaginationUtils;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * APIs for my coupons.
 */
@Controller
@RequestMapping(value = "/{baseSiteId}/users/{userId}/customercoupons")
@Api(tags = "Customer Coupons")
public class CustomerCouponsController
{

	private static final String MAX_PAGE_SIZE_KEY = "webservicescommons.pagination.maxPageSize";
	
	@Resource(name = "customerCouponFacade")
	private CustomerCouponFacade customerCouponFacade;

	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;

	@Resource(name = "customerNotificationPreferenceCheckStrategy")
	private CustomerNotificationPreferenceCheckStrategy customerNotificationPreferenceCheckStrategy;

	@Resource(name = "dataMapper")
	private DataMapper dataMapper;

	@Resource(name = "webPaginationUtils")
	private WebPaginationUtils webPaginationUtils;

	@Resource(name = "customerCouponCodeValidator")
	private Validator customerCouponCodeValidator;

	@Resource(name = "customerCouponValidator")
	private Validator customerCouponValidator;

	@Resource(name = "customerCouponAssignResultValidator")
	private Validator customerCouponAssignResultValidator;

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@ResponseBody
	@GetMapping
	@ApiOperation(value = "Gets all customer coupons of the current customer.", notes = "Gets the customer coupon list of the current customer.")
	@ApiBaseSiteIdAndUserIdParam
	public CustomerCouponSearchResultWsDTO getCustomerCoupons(
			@ApiParam(value = "The current result page requested.") @RequestParam(name = "currentPage", required = false, defaultValue = "0") final int currentPage,
			@ApiParam(value = "The number of results returned per page.") @RequestParam(name = "pageSize", required = false, defaultValue = "10") final int pageSize,
			@ApiParam(value = "The sorting method applied to the return results.") @RequestParam(name = "sort", required = false) final String sort,
			@ApiParam(value = "The flag for indicating if total number of results is needed or not.", allowableValues = "true,false") @RequestParam(name = "needsTotal", required = false, defaultValue = "true") final boolean needsTotal,
			@ApiFieldsParam @RequestParam(defaultValue = "DEFAULT") final String fields)
	{
		final SearchPageData searchPageData = getWebPaginationUtils().buildSearchPageData(sort, currentPage, pageSize, needsTotal);
		recalculatePageSize(searchPageData);

		return getDataMapper().map(getCustomerCouponFacade().getPaginatedCoupons(searchPageData),
				CustomerCouponSearchResultWsDTO.class, fields);
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@PostMapping("/{couponCode}/claim")
	@ApiOperation(value = "Claims a customer coupon.", notes = "Claims a customer coupon by coupon code.")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	public CustomerCoupon2CustomerWsDTO doClaimCustomerCoupon(
			@ApiParam(value = "Coupon code", required = true) @PathVariable final String couponCode,
			@ApiFieldsParam @RequestParam(defaultValue = "DEFAULT") final String fields)
	{
		final AssignCouponResult result = getCustomerCouponFacade().grantCouponAccessForCurrentUser(couponCode);
		validateCoupon(customerCouponAssignResultValidator, result, "couponAssignResult");

		final CustomerCouponData coupon = getCustomerCouponFacade().getCustomerCouponForCode(couponCode);
		final CustomerCoupon2CustomerWsDTO relation = new CustomerCoupon2CustomerWsDTO();
		relation.setCustomer(getCustomerWsDTO(fields));
		relation.setCoupon(getDataMapper().map(coupon, CustomerCouponWsDTO.class, fields));

		return relation;
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@PostMapping(value = "/{couponCode}/notification")
	@ApiOperation(value = "Subscribes to a coupon notification.", notes = "Make a subscription to a customer coupon to receive notifications when it will be in effect soon, or will expire soon.")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	public CustomerCouponNotificationWsDTO doSubscribeToCustomerCoupon(
			@ApiParam(value = "Coupon code", required = true) @PathVariable final String couponCode,
			@ApiFieldsParam @RequestParam(defaultValue = "DEFAULT")final String fields)
	{

		validateCoupon(customerCouponValidator, couponCode, "couponCode");

		final CustomerCouponNotificationData notification = getCustomerCouponFacade().saveCouponNotification(couponCode);
		getCustomerNotificationPreferenceCheckStrategy().checkCustomerNotificationPreference();

		return getDataMapper().map(notification, CustomerCouponNotificationWsDTO.class, fields);
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@ResponseStatus(code = HttpStatus.OK)
	@DeleteMapping(value = "/{couponCode}/notification")
	@ApiOperation(value = "Unsubscribes from the coupon notification.", notes = "Removes notification subscription from the specific customer coupon.")
	@ApiBaseSiteIdAndUserIdParam
	public void doUnsubscribeFromCustomerCoupon(
			@ApiParam(value = "Coupon code", required = true) @PathVariable final String couponCode)
	{
		validateCoupon(customerCouponCodeValidator, couponCode, "couponCode");
		getCustomerCouponFacade().removeCouponNotificationByCode(couponCode);
	}

	protected void validateCoupon(final Validator validator, final Object obj, final String objName)
	{
		final Errors errors = new BeanPropertyBindingResult(obj, objName);
		validator.validate(obj, errors);
		if (errors.hasErrors())
		{
			throw new WebserviceValidationException(errors);
		}
	}

	protected void recalculatePageSize(final SearchPageData searchPageData)
	{
		int pageSize = searchPageData.getPagination().getPageSize();
		if (pageSize <= 0)
		{
			final int maxPageSize = Config.getInt(MAX_PAGE_SIZE_KEY, 1000);
			pageSize = getWebPaginationUtils().getDefaultPageSize();
			pageSize = pageSize > maxPageSize ? maxPageSize : pageSize;
			searchPageData.getPagination().setPageSize(pageSize);
		}
	}

	protected UserWsDTO getCustomerWsDTO(final String fields)
	{
		return getDataMapper().map(getCustomerFacade().getCurrentCustomer(), UserWsDTO.class, fields);
	}

	protected CustomerCouponFacade getCustomerCouponFacade()
	{
		return customerCouponFacade;
	}

	protected DataMapper getDataMapper()
	{
		return dataMapper;
	}

	protected CustomerNotificationPreferenceCheckStrategy getCustomerNotificationPreferenceCheckStrategy()
	{
		return customerNotificationPreferenceCheckStrategy;
	}

	protected WebPaginationUtils getWebPaginationUtils()
	{
		return webPaginationUtils;
	}

	protected CustomerFacade getCustomerFacade()
	{
		return customerFacade;
	}

}
