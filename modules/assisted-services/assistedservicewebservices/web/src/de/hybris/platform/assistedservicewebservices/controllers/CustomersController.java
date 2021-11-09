/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.assistedservicewebservices.controllers;

import static de.hybris.platform.assistedservicewebservices.constants.AssistedservicewebservicesConstants.DEFAULT_CURRENT_PAGE;
import static de.hybris.platform.assistedservicewebservices.constants.AssistedservicewebservicesConstants.DEFAULT_PAGE_SIZE;
import static de.hybris.platform.assistedservicewebservices.constants.AssistedservicewebservicesConstants.QUERY;

import de.hybris.platform.assistedservicefacades.AssistedServiceFacade;
import de.hybris.platform.assistedservicefacades.user.data.AutoSuggestionCustomerData;
import de.hybris.platform.assistedserviceservices.exception.AssistedServiceCartBindException;
import de.hybris.platform.assistedserviceservices.exception.AssistedServiceException;
import de.hybris.platform.assistedservicewebservices.dto.CustomerSearchPageWsDTO;
import de.hybris.platform.assistedservicewebservices.dto.CustomerSuggestionWsDto;
import de.hybris.platform.assistedservicewebservices.helper.CustomerHelper;
import de.hybris.platform.assistedservicewebservices.utils.PaginationUtils;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.util.Config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


@Controller
public class CustomersController extends AbstractAssistedServiceWebServiceController
{
	@Resource(name = "assistedServiceFacade")
	private AssistedServiceFacade assistedServiceFacade;

	@Resource(name = "customerHelper")
	private CustomerHelper customerHelper;

	@ApiOperation(value = "Returns customers based on query parameters", notes = "This endpoint returns paginated list of customers based on provided query parameters. If query term is present it will return customers based on provided value. If customerListId is present it will ignore query term and return only customers who belong to the given customer list. If orderId parameter is present it will ignore previous parameters and it will return customer associated to the given order.This can only be done by the logged in user.")
	@RequestMapping(value = "/customers/search", method = RequestMethod.GET)
	@ResponseBody
	public CustomerSearchPageWsDTO getPageableCustomers(
			@ApiParam(value = "Customer uid search term", required = false) @RequestParam(required = false) final String query,
			@ApiParam(value = "Current page", required = false) @RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@ApiParam(value = "Page size", required = false) @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@ApiParam(value = "Id of the customer list", required = false) @RequestParam(required = false) final String customerListId,
			@ApiParam(value = "Id of the order", required = false) @RequestParam(required = false) final String orderId,
			@ApiParam(value = "Id of the BaseSite", required = true) @RequestParam(required = true) final String baseSite,
			@ApiParam(value = "Sort parameter. Possible values: byUidAsc, byUidDesc,  byNameAsc, byNameDesc", required = false) @RequestParam(required = false) final String sort)
	{
		SearchPageData<CustomerData> customerSearchPageData;

		final PageableData pageableData = PaginationUtils.createPageableData(currentPage, pageSize, sort);

		if (StringUtils.isNotBlank(orderId))
		{
			customerSearchPageData = searchCustomerByOrder(orderId, pageableData);
		}
		else
		{
			if (StringUtils.isNotBlank(customerListId))
			{
				customerSearchPageData = getCustomersFromCustomerList(customerListId, query, pageableData);
			}
			else
			{
				customerSearchPageData = searchCustomersByQuery(query, pageableData);
			}
		}

		return getCustomerHelper().getCustomerSearchPageDto(customerSearchPageData);
	}

	@ApiOperation(value = "Returns customers to auto complete based on customerQuery parameter")
	@RequestMapping(value = "/customers/autocomplete", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<CustomerSuggestionWsDto> autoComplete(
			@ApiParam(value = "Customer query: first characters of customer email or part of name. A BadRequestException is thrown when the query is too short. Minimum query length can be updated via `assistedserviceswebservices.customer.suggestions.minimum.query.length` property.", required = true) @RequestParam(required = true) final String customerQuery,
			@ApiParam(value = "Id of the BaseSite", required = true) @RequestParam(required = true) final String baseSite)
	{
		final int minQueryLength = Config.getInt("assistedserviceswebservices.customer.suggestions.minimum.query.length", 3);

		if (minQueryLength > customerQuery.length())
		{
			throw new BadRequestException("Customer query length must not be less than " + minQueryLength);
		}

		final List<AutoSuggestionCustomerData> customerSuggestions = getAssistedServiceFacade().getCustomerSuggestions(customerQuery);

		return getCustomerHelper().getCustomerSuggestions(customerSuggestions);
	}

	@ApiOperation(value = "Binds customer with provided id to cart if it's anonymous cart")
	@RequestMapping(value = "/bind-cart", method = RequestMethod.POST)
	public ResponseEntity<String> bindCart(@ApiParam(value = "Id of the Customer", required = true) @RequestParam(value = "customerId") final String customerId,
										   @ApiParam(value = "Id of the anonymous Cart", required = true) @RequestParam(value = "cartId") final String cartId,
										   @ApiParam(value = "Id of the BaseSite", required = true) @RequestParam final String baseSite)
	{
		try
		{
			getAssistedServiceFacade().bindCustomerToCartWithoutEmulating(customerId, cartId);
			return ResponseEntity.status(HttpStatus.OK).build();
		}
		catch (final AssistedServiceCartBindException e)
		{
			throw new BadRequestException(e.getMessage());
		}
		catch (final AssistedServiceException e)
		{
			throw new InternalServerErrorException(e.getMessage());
		}
	}

	protected SearchPageData<CustomerData> getCustomersFromCustomerList(final String customerListId, final String query,
			final PageableData pageableData)
	{
		final Map<String, Object> parametersMap = new HashMap<>();

		if (StringUtils.isNotBlank(query))
		{
			parametersMap.put(QUERY, query);
		}
		return getCustomerListFacade().getPagedCustomersForCustomerListUID(customerListId,
				getCustomerFacade().getCurrentCustomerUid(), pageableData, parametersMap);
	}

	protected SearchPageData<CustomerData> searchCustomersByQuery(final String query, final PageableData pageableData)
	{
		final List<CustomerData> customers = assistedServiceFacade.getCustomers(query, pageableData);
		return createSearchPageData(customers, PaginationUtils.buildPaginationData(pageableData, customers));
	}

	protected SearchPageData<CustomerData> searchCustomerByOrder(final String orderId, final PageableData pageableData)
	{
		final List<CustomerData> customers = Arrays.asList(assistedServiceFacade.getCustomerByOrder(orderId));
		return createSearchPageData(customers, PaginationUtils.buildPaginationData(pageableData, customers));
	}

	public AssistedServiceFacade getAssistedServiceFacade()
	{
		return assistedServiceFacade;
	}

	public CustomerHelper getCustomerHelper()
	{
		return customerHelper;
	}
}
