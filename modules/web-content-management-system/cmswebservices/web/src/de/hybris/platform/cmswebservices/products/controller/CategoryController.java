/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices.products.controller;

import static de.hybris.platform.cmswebservices.constants.CmswebservicesConstants.API_VERSION;

import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cmsfacades.data.CategoryData;
import de.hybris.platform.cmsfacades.products.ProductSearchFacade;
import de.hybris.platform.cmswebservices.dto.CategoryDataListWsDTO;
import de.hybris.platform.cmswebservices.dto.CategoryWsDTO;
import de.hybris.platform.cmswebservices.dto.PageableWsDTO;
import de.hybris.platform.cmswebservices.security.IsAuthorizedCmsManager;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.pagination.WebPaginationUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import io.swagger.annotations.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import io.swagger.annotations.Api;

/**
 * Controller to retrieve and search Product Categories within a Product Catalog Version.
 */
@Controller
@IsAuthorizedCmsManager
@RequestMapping(API_VERSION + "/productcatalogs/{catalogId}/versions/{versionId}/categories")
@Api(tags = "product categories")
public class CategoryController
{

	@Resource
	private ProductSearchFacade cmsProductSearchFacade;

	@Resource
	private WebPaginationUtils webPaginationUtils;

	@Resource
	private DataMapper dataMapper;

	@RequestMapping(value = "/{code}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Gets category by code.", notes = "Finds a specific category that matches given product category code.",
					nickname = "getCategoryByCode")
	@ApiImplicitParams(
	{ @ApiImplicitParam(name = "catalogId", value = "The catalog id", required = true, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "versionId", value = "The catalog version identifier", required = true, dataType = "string", paramType = "path") })
	@ApiResponses(
	{ @ApiResponse(code = 400, message = "When the item has not been found (CMSItemNotFoundException) or when there was problem during conversion (ConversionException)."),
			@ApiResponse(code = 200, message = "CategoryData", response = CategoryWsDTO.class) })
	public CategoryWsDTO getCategoryByCode(@ApiParam(value = "Category code", required = true)
	@PathVariable
	final String code) throws CMSItemNotFoundException, ConversionException
	{
		return getDataMapper().map(getCmsProductSearchFacade().getProductCategoryByCode(code), CategoryWsDTO.class);
	}

	@RequestMapping(method = RequestMethod.GET, params =
	{ "pageSize" })
	@ResponseBody
	@ApiOperation(value = "Finds product categories by text.", notes = "Retrieves a list of available product categories that match a free text search field.",
					nickname = "getProductCategoriesByText")
	@ApiResponses(
	{ @ApiResponse(code = 200, message = "DTO which serves as a wrapper object that contains a list of CategoryData, never null.", response = CategoryDataListWsDTO.class) })
	@ApiImplicitParams(
	{ @ApiImplicitParam(name = "catalogId", value = "The catalog id", required = true, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "versionId", value = "The catalog version identifier", required = true, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "pageSize", value = "The maximum number of elements in the result list.", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "currentPage", value = "The requested page number", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "sort", value = "The string field the results will be sorted with", required = false, dataType = "string", paramType = "query") })
	public CategoryDataListWsDTO findProductCategoriesByText(
			@ApiParam(value = "The string value on which products will be filtered. Deprecated, use mask instead!", required = false)
			@RequestParam(required = false)
			final String text, @ApiParam(value = "The string value on which products will be filtered", required = false)
			@RequestParam(required = false)
			final String mask, @ApiParam(value = "Defines the pageSize, currentPage and sorting order", required = true)
			@ModelAttribute
			final PageableWsDTO pageableInfo)
	{
		if (Objects.nonNull(text) && Objects.nonNull(mask))
		{
			throw new IllegalArgumentException("Invalid query parameters. text and mask cannot be used together, use mask instead.");
		}
		final String filterAttribute = Optional.ofNullable(text).orElse(mask);

		final SearchResult<CategoryData> productSearchResult = getCmsProductSearchFacade().findProductCategories(filterAttribute,
				Optional.of(pageableInfo).map(pageableWsDTO -> getDataMapper().map(pageableWsDTO, PageableData.class)).get());

		final CategoryDataListWsDTO productList = new CategoryDataListWsDTO();
		productList.setProductCategories(productSearchResult //
				.getResult() //
				.stream() //
				.map(productData -> getDataMapper().map(productData, CategoryWsDTO.class)) //
				.collect(Collectors.toList()));
		productList.setPagination(getWebPaginationUtils().buildPagination(productSearchResult));
		return productList;
	}

	protected ProductSearchFacade getCmsProductSearchFacade()
	{
		return cmsProductSearchFacade;
	}

	public void setCmsProductSearchFacade(final ProductSearchFacade cmsProductSearchFacade)
	{
		this.cmsProductSearchFacade = cmsProductSearchFacade;
	}

	protected WebPaginationUtils getWebPaginationUtils()
	{
		return webPaginationUtils;
	}

	public void setWebPaginationUtils(final WebPaginationUtils webPaginationUtils)
	{
		this.webPaginationUtils = webPaginationUtils;
	}

	protected DataMapper getDataMapper()
	{
		return dataMapper;
	}

	public void setDataMapper(final DataMapper dataMapper)
	{
		this.dataMapper = dataMapper;
	}
}
