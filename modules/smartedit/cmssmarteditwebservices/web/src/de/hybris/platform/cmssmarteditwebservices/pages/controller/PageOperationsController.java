/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmssmarteditwebservices.pages.controller;

import static de.hybris.platform.cmssmarteditwebservices.constants.CmssmarteditwebservicesConstants.API_VERSION;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cmsfacades.data.CMSPageOperationsData;
import de.hybris.platform.cmsfacades.pages.PageFacade;
import de.hybris.platform.cmssmarteditwebservices.dto.CMSPageOperationWsDTO;
import de.hybris.platform.webservicescommons.mapping.DataMapper;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
 * Controller that provides an API to perform different operations on pages.
 */
@Controller
@RequestMapping(API_VERSION + "/sites/{baseSiteId}/catalogs/{catalogId}/pages/{pageId}/operations")
@Api(tags = "page operations")
public class PageOperationsController
{

	@Resource
	private DataMapper dataMapper;

	@Resource
	private PageFacade pageFacade;

	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiOperation(value = "Perform different operations on the page item.", notes = "Endpoint to perform different operations on the page item such as delete a page etc.")
	@ApiImplicitParams(
	{
			@ApiImplicitParam(name = "baseSiteId", value = "Base site identifier", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "catalogId", value = "The uid of the catalog", required = true, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "pageUid", value = "The uid of the page", required = true, dataType = "string", paramType = "path")
	})
	@ApiResponses(
	{
			@ApiResponse(code = 404, message = "When the item has not been found (UnknownIdentifierException) "),
			@ApiResponse(code = 400, message = "When the payload does not have the 'operation' property. (IllegalArgumentException)"),
			@ApiResponse(code = 200, message = "The page operation item.", response = CMSPageOperationWsDTO.class)
	})
	public CMSPageOperationWsDTO perform( //
			@ApiParam(value = "The uid of the catalog", required = true)
			@PathVariable
			final String catalogId, //
			@ApiParam(value = "The uid of the page to be updated", required = true)
			@PathVariable
			final String pageId,
			@ApiParam(value = "The DTO object containing all the information about operation to be performed", required = true)
			@RequestBody
			final CMSPageOperationWsDTO dto) throws CMSItemNotFoundException
	{
		final CMSPageOperationsData data = getDataMapper().map(dto, CMSPageOperationsData.class);
		data.setCatalogId(catalogId);
		final CMSPageOperationsData newPageOperationData = getPageFacade().performOperation(pageId, data);
		return getDataMapper().map(newPageOperationData, CMSPageOperationWsDTO.class);
	}

	protected DataMapper getDataMapper()
	{
		return dataMapper;
	}

	public void setDataMapper(final DataMapper dataMapper)
	{
		this.dataMapper = dataMapper;
	}

	protected PageFacade getPageFacade()
	{
		return pageFacade;
	}

	public void setPageFacade(final PageFacade pageFacade)
	{
		this.pageFacade = pageFacade;
	}

}
