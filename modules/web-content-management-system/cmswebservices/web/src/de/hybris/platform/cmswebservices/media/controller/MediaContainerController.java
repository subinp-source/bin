/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices.media.controller;

import static de.hybris.platform.cmswebservices.constants.CmswebservicesConstants.API_VERSION;

import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cmsfacades.data.MediaContainerData;
import de.hybris.platform.cmsfacades.mediacontainers.MediaContainerFacade;
import de.hybris.platform.cmswebservices.dto.MediaContainerListWsDTO;
import de.hybris.platform.cmswebservices.dto.MediaContainerWsDTO;
import de.hybris.platform.cmswebservices.dto.PageableWsDTO;
import de.hybris.platform.cmswebservices.security.IsAuthorizedCmsManager;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.pagination.WebPaginationUtils;

import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
 * Controller that handles searching and creating media container
 */
@RestController
@IsAuthorizedCmsManager
@RequestMapping(API_VERSION + "/catalogs/{catalogId}/versions/{versionId}/mediacontainers")
@Api(tags = "media containers")
public class MediaContainerController
{
	@Resource
	private MediaContainerFacade mediaContainerFacade;

	@Resource
	private DataMapper dataMapper;

	@Resource
	private WebPaginationUtils webPaginationUtils;

	@GetMapping(value = "/{code}")
	@ResponseBody
	@ApiOperation(value = "Gets media container by code.", notes = "Retrieves a specific media container that matches the given id.", nickname = "getMediaContainerByCode")
	@ApiResponses(
	{ //
			@ApiResponse(code = 400, message = "When the media container was not found (CMSItemNotFoundException) or when there was a problem during conversion (ConversionException)."),
			@ApiResponse(code = 200, message = "MediaContainerWsDTO", response = MediaContainerWsDTO.class) //
	})
	@ApiImplicitParams(
	{ //
					@ApiImplicitParam(name = "catalogId", value = "The catalog id", required = true, dataType = "string", paramType = "path"),
					@ApiImplicitParam(name = "versionId", value = "The catalog version identifier", required = true, dataType = "string", paramType = "path")
	})
	public MediaContainerWsDTO getMediaContainerByCode(
			@ApiParam(value = "The unique identifier of the media container", required = true)
			@PathVariable
			final String code) throws CMSItemNotFoundException
	{
		return getDataMapper().map(getMediaContainerFacade().getMediaContainerForQualifier(code), MediaContainerWsDTO.class);
	}

	@GetMapping(params =	{ "pageSize" })
	@ResponseBody
	@ApiOperation(value = "Finds media container by partial to full code matching.", notes = "Retrieves a list of available media containers using a free text search field.", nickname = "getMediaContainersByText")
	@ApiResponses(
	{ //
			@ApiResponse(code = 200, message = "Item which serves as a wrapper object that contains a list of MediaContainerData; never null", response = MediaContainerListWsDTO.class) })
	@ApiImplicitParams(
	{ //
			@ApiImplicitParam(name = "catalogId", value = "The catalog id", required = true, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "versionId", value = "The catalog version identifier", required = true, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "pageSize", value = "The maximum number of elements in the result list.", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "currentPage", value = "The requested page number", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "sort", value = "The string field the results will be sorted with", required = false, dataType = "string", paramType = "query") //
	})
	public MediaContainerListWsDTO findMediaContainersByText(
			@ApiParam(value = "The string value on which media containers will be filtered", required = false)
			@RequestParam(required = false)
			final String mask, @ApiParam(value = "PageableWsDTO", required = true)
			@ModelAttribute
			final PageableWsDTO pageableInfo)
	{
		final SearchResult<MediaContainerData> mediaContainerSearchResult = getMediaContainerFacade().findMediaContainers(mask,
				Optional.of(pageableInfo).map(pageableWsDTO -> getDataMapper().map(pageableWsDTO, PageableData.class)).get());

		final MediaContainerListWsDTO mediaContainers = new MediaContainerListWsDTO();
		mediaContainers.setMediaContainers(mediaContainerSearchResult //
				.getResult() //
				.stream() //
				.map(containerData -> getDataMapper().map(containerData, MediaContainerWsDTO.class)) //
				.collect(Collectors.toList()));
		mediaContainers.setPagination(getWebPaginationUtils().buildPagination(mediaContainerSearchResult));
		return mediaContainers;
	}

	protected MediaContainerFacade getMediaContainerFacade()
	{
		return mediaContainerFacade;
	}

	public void setMediaContainerFacade(final MediaContainerFacade mediaContainerFacade)
	{
		this.mediaContainerFacade = mediaContainerFacade;
	}

	protected DataMapper getDataMapper()
	{
		return dataMapper;
	}

	public void setDataMapper(final DataMapper dataMapper)
	{
		this.dataMapper = dataMapper;
	}

	protected WebPaginationUtils getWebPaginationUtils()
	{
		return webPaginationUtils;
	}

	public void setWebPaginationUtils(final WebPaginationUtils webPaginationUtils)
	{
		this.webPaginationUtils = webPaginationUtils;
	}
}
