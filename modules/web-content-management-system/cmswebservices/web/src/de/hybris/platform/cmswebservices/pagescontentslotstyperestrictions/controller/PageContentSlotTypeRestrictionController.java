/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices.pagescontentslotstyperestrictions.controller;

import static de.hybris.platform.cmswebservices.constants.CmswebservicesConstants.API_VERSION;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cmsfacades.CMSPageContentSlotListData;
import de.hybris.platform.cmsfacades.data.ContentSlotTypeRestrictionsData;
import de.hybris.platform.cmsfacades.exception.ValidationException;
import de.hybris.platform.cmsfacades.pagescontentslotstyperestrictions.PageContentSlotTypeRestrictionsFacade;
import de.hybris.platform.cmswebservices.dto.CMSContentSlotIdListWsDTO;
import de.hybris.platform.cmswebservices.dto.ContentSlotTypeRestrictionsWsDTO;
import de.hybris.platform.cmswebservices.security.IsAuthorizedCmsManager;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Api;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Controller that provides type restrictions for CMS content slots.
 *
 */
@Controller
@IsAuthorizedCmsManager
@RequestMapping(API_VERSION + "/catalogs/{catalogId}/versions/{versionId}/pages/{pageId}")
@Api(tags = "page slot restrictions")
public class PageContentSlotTypeRestrictionController
{
	@Resource
	private PageContentSlotTypeRestrictionsFacade pageContentSlotTypeRestrictionsFacade;

	@Resource
	private DataMapper dataMapper;

	@RequestMapping(value = "/contentslots/{slotId}/typerestrictions", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Gets type restrictions for content slot.", notes = "Retrieves a list of available type restrictions for a given page id and content slot id.",
					nickname = "getTypeRestrictionForContentSlot")
	@ApiResponses(
	{
			@ApiResponse(code = 400, message = "When the page/slot cannot be found (CMSItemNotFoundException)"),
			@ApiResponse(code = 200, message = "DTO providing the mapping", response = ContentSlotTypeRestrictionsWsDTO.class)
	})
	@ApiImplicitParams(
	{
			@ApiImplicitParam(name = "catalogId", value = "The catalog id", required = true, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "versionId", value = "The catalog version identifier", required = true, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "pageId", value = "The page identifier", required = true, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "slotId", value = "The slot identifier", required = true, dataType = "string", paramType = "path") })
	public ContentSlotTypeRestrictionsWsDTO getTypeRestrictionsForContentSlot(
			@ApiParam(value = "Page identifier", required = true) final @PathVariable String pageId,
			@ApiParam(value = "Content slot identifier", required = true) final @PathVariable String slotId)
			throws CMSItemNotFoundException
	{
		final ContentSlotTypeRestrictionsData data = getPageContentSlotTypeRestrictionsFacade()
				.getTypeRestrictionsForContentSlotUID(pageId, slotId);

		return getDataMapper().map(data, ContentSlotTypeRestrictionsWsDTO.class);
	}

	@RequestMapping(value = "/typerestrictions", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Gets type restrictions for the provided content slots.", notes = "Retrieves a list of available type restrictions for the given slotId's.",
			nickname = "searchTypeRestrictionBySlotIds")
	@ApiResponses(
			{
					@ApiResponse(code = 400, message = "When slot(s) for slotId(s) cannot be found (CMSItemNotFoundException)"),
					@ApiResponse(code = 200, message = "DTO providing the mapping", response = ContentSlotTypeRestrictionsWsDTO.class)
			})
	@ApiImplicitParams(
			{
					@ApiImplicitParam(name = "catalogId", value = "The catalog id", required = true, dataType = "string", paramType = "path"),
					@ApiImplicitParam(name = "versionId", value = "The catalog version identifier", required = true, dataType = "string", paramType = "path"),
					@ApiImplicitParam(name = "pageId", value = "The page identifier", required = true, dataType = "string", paramType = "path") })
	public List<ContentSlotTypeRestrictionsWsDTO> searchTypeRestrictionBySlotIds(
			@ApiParam(value = "Page identifier", required = true) //
			final @PathVariable String pageId, //
			@ApiParam(value = "Map representing the content slots for which to retrieve type restrictions", required = true) //
			@RequestBody CMSContentSlotIdListWsDTO data)
			throws CMSItemNotFoundException
	{
		try
		{
			final CMSPageContentSlotListData contentSlotListData = getDataMapper().map(data, CMSPageContentSlotListData.class);
			contentSlotListData.setPageId(pageId);

			return getPageContentSlotTypeRestrictionsFacade()
					.getTypeRestrictionsForContentSlots(contentSlotListData)
					.stream()
					.map(restriction -> getDataMapper().map(restriction, ContentSlotTypeRestrictionsWsDTO.class))
					.collect(Collectors.toList());
		}
		catch (final ValidationException e)
		{
			throw new WebserviceValidationException(e.getValidationObject());
		}
	}

	protected DataMapper getDataMapper()
	{
		return dataMapper;
	}

	public void setDataMapper(final DataMapper dataMapper)
	{
		this.dataMapper = dataMapper;
	}


	protected PageContentSlotTypeRestrictionsFacade getPageContentSlotTypeRestrictionsFacade()
	{
		return pageContentSlotTypeRestrictionsFacade;
	}

	public void setPageContentSlotTypeRestrictionsFacade(
			final PageContentSlotTypeRestrictionsFacade pageContentSlotTypeRestrictionsFacade)
	{
		this.pageContentSlotTypeRestrictionsFacade = pageContentSlotTypeRestrictionsFacade;
	}
}
