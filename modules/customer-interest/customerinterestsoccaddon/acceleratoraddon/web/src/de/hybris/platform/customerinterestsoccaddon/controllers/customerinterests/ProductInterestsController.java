/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customerinterestsoccaddon.controllers.customerinterests;

import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.customerinterestsfacades.data.ProductInterestData;
import de.hybris.platform.customerinterestsfacades.data.ProductInterestRelationData;
import de.hybris.platform.customerinterestsfacades.productinterest.ProductInterestFacade;
import de.hybris.platform.customerinterestsoccaddon.dto.CustomerInterestsSearchResultWsDTO;
import de.hybris.platform.customerinterestsoccaddon.dto.ProductInterestRelationWsDTO;
import de.hybris.platform.customerinterestsoccaddon.validation.ProductInterestsValidator;
import de.hybris.platform.notificationservices.enums.NotificationType;
import de.hybris.platform.util.Config;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;
import de.hybris.platform.webservicescommons.pagination.WebPaginationUtils;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * Provides RESTful API for product interests related methods
 */
@Controller
@RequestMapping("/{baseSiteId}/users/{userId}/productinterests")
@Api(tags = "Product Interests")
public class ProductInterestsController
{
	private static final String MAX_PAGE_SIZE_KEY = "webservicescommons.pagination.maxPageSize";
	private static final String REQUESTPARAM = "RequestParam";
	private static final String DEFAULT_FIELD_SET = FieldSetLevelHelper.DEFAULT_LEVEL;

	@Resource(name = "dataMapper")
	private DataMapper dataMapper;

	@Resource(name = "productInterestFacade")
	private ProductInterestFacade productInterestFacade;

	@Resource(name = "webPaginationUtils")
	private WebPaginationUtils webPaginationUtils;

	@Resource(name = "productInterestsValidator")
	private ProductInterestsValidator productInterestsValidator;

	@Resource(name = "productFacade")
	private ProductFacade productFacade;


	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	@ApiOperation(value = "Sets product interests for a user.", notes = "Sets product interests for a user.")
	@ApiBaseSiteIdAndUserIdParam
	public ProductInterestRelationWsDTO createProductInterest(
			@ApiParam(value = "Product identifier", required = true) @RequestParam(required = true) final String productCode,
			@ApiParam(value = "Notification type", required = true) @RequestParam(required = true) String notificationType,
			@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		notificationType = StringUtils.upperCase(notificationType);
		final Errors errors = new BeanPropertyBindingResult(REQUESTPARAM, REQUESTPARAM);
		getProductInterestsValidator().validateProductInterestCreation(productCode, notificationType, errors);

		final ProductInterestRelationData productInterestRelation = saveProductInterest(productCode,
				NotificationType.valueOf(notificationType));

		return dataMapper.map(productInterestRelation, ProductInterestRelationWsDTO.class, fields);
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Removes product interests by product code and notification type", notes = "Removes product interests by product code and notification type.")
	@ApiBaseSiteIdAndUserIdParam
	public void removeProductInterest(
			@ApiParam(value = "Product identifier", required = true) @RequestParam(required = true) final String productCode,
			@ApiParam(value = "Notification type", required = true) @RequestParam(required = true) String notificationType)
	{
		notificationType = StringUtils.upperCase(notificationType);
		final Errors errors = new BeanPropertyBindingResult(REQUESTPARAM, REQUESTPARAM);
		getProductInterestsValidator().validateProductInterestRemoval(productCode, notificationType, errors);

		final List<ProductInterestData> productInterestsData = getProductInterestFacade()
				.getProductInterestsForNotificationType(productCode, NotificationType.valueOf(notificationType));

		productInterestsData.stream().forEach(x -> getProductInterestFacade().removeAllProductInterests(x.getProduct().getCode()));
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	@ApiOperation(value = "Gets product interests for a user.", notes = "Gets product interests for a user.")
	@ApiImplicitParams(
	{ @ApiImplicitParam(name = "pageSize", value = "The number of results returned per page.", defaultValue = "20", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "currentPage", value = "The current result page requested.", defaultValue = "0", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "sort", value = "Sorting method applied to the return results.", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "needsTotal", value = "the flag for indicating if total number of results is needed or not", defaultValue = "true", allowableValues = "true,false", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "userId", value = "User identifier or one of the literals : \'current\' for currently authenticated user, \'anonymous\' for anonymous user", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "baseSiteId", value = "Base site identifier.", required = true, dataType = "String", paramType = "path") })

	public CustomerInterestsSearchResultWsDTO getProductInterests(final HttpServletRequest request,
			@ApiParam(value = "Product identifier", required = false) @RequestParam(required = false) final String productCode,
			@ApiParam(value = "Notification type", required = false) @RequestParam(required = false) String notificationType,
			@ApiFieldsParam @RequestParam(defaultValue = "DEFAULT") final String fields)
	{
		notificationType = StringUtils.upperCase(notificationType);
		final Errors errors = new BeanPropertyBindingResult(REQUESTPARAM, REQUESTPARAM);
		getProductInterestsValidator().validateProductInterestRetrieval(productCode, notificationType, errors);

		final Map<String, String> params = getParameterMapFromRequest(request);

		final SearchPageData<Object> searchPageData = webPaginationUtils.buildSearchPageData(params);
		recalculatePageSize(searchPageData);
		getProductInterestsValidator().validatePageSize(searchPageData.getPagination().getPageSize(), errors);

		final SearchPageData<ProductInterestRelationData> paginatedData = productInterestFacade
				.getPaginatedProductInterestsForNotificationType(productCode,
						StringUtils.isNoneBlank(notificationType) ? NotificationType.valueOf(notificationType) : null, searchPageData);

		return dataMapper.map(paginatedData, CustomerInterestsSearchResultWsDTO.class, fields);
	}

	protected ProductInterestRelationData saveProductInterest(final String productCode, final NotificationType notificationType)
	{
		final ProductInterestData productInterestData = getProductInterestFacade()
				.getProductInterestDataForCurrentCustomer(productCode, notificationType).orElse(new ProductInterestData());
		if (ObjectUtils.isEmpty(productInterestData.getProduct()))
		{
			final ProductData product = getProductFacade().getProductForCodeAndOptions(productCode,
					Arrays.asList(ProductOption.BASIC));
			productInterestData.setProduct(product);
			productInterestData.setNotificationType(notificationType);

			getProductInterestFacade().saveProductInterest(productInterestData);
		}
		return getProductInterestFacade().getProductInterestRelation(productCode);
	}

	protected Map<String, String> getParameterMapFromRequest(final HttpServletRequest request)
	{
		final Map<String, String[]> parameterMap = request.getParameterMap();
		final Map<String, String> result = new LinkedHashMap<String, String>();
		if (MapUtils.isEmpty(parameterMap))
		{
			return result;
		}
		for (final Map.Entry<String, String[]> entry : parameterMap.entrySet())
		{
			if (entry.getValue().length > 0)
			{
				result.put(entry.getKey(), entry.getValue()[0]);
			}
		}
		return result;
	}

	protected void recalculatePageSize(final SearchPageData searchPageData)
	{
		int pageSize = searchPageData.getPagination().getPageSize();
		if (pageSize <= 0)
		{
			final int maxPageSize = Config.getInt(MAX_PAGE_SIZE_KEY, 1000);
			pageSize = webPaginationUtils.getDefaultPageSize();
			pageSize = pageSize > maxPageSize ? maxPageSize : pageSize;
			searchPageData.getPagination().setPageSize(pageSize);
		}
	}

	protected ProductInterestFacade getProductInterestFacade()
	{
		return productInterestFacade;
	}

	protected ProductInterestsValidator getProductInterestsValidator()
	{
		return productInterestsValidator;
	}

	protected ProductFacade getProductFacade()
	{
		return productFacade;
	}

	protected void handleErrors(final Errors errors)
	{
		if (errors.hasErrors())
		{
			throw new WebserviceValidationException(errors);
		}
	}

}
