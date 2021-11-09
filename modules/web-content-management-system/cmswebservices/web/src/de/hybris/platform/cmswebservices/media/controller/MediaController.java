/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices.media.controller;

import static de.hybris.platform.cmswebservices.constants.CmswebservicesConstants.API_VERSION;

import de.hybris.platform.cmsfacades.exception.ValidationException;
import de.hybris.platform.cmsfacades.media.MediaFacade;
import de.hybris.platform.cmswebservices.controller.AbstractSearchableController;
import de.hybris.platform.cmswebservices.data.MediaData;
import de.hybris.platform.cmswebservices.data.MediaListData;
import de.hybris.platform.cmswebservices.data.NamedQueryData;
import de.hybris.platform.cmswebservices.dto.CMSItemUuidListWsDTO;
import de.hybris.platform.cmswebservices.security.IsAuthorizedCmsManager;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import springfox.documentation.annotations.ApiIgnore;


/**
 * Controller that handles searching for media.
 */
@Controller
@IsAuthorizedCmsManager
@RequestMapping(API_VERSION + "/media")
@Api(tags = "media")
public class MediaController extends AbstractSearchableController
{
	private static Logger LOGGER = LoggerFactory.getLogger(MediaController.class);

	@Resource
	private MediaFacade mediaFacade;

	@Resource
	private DataMapper dataMapper;

	@RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Gets media by uuid.", notes = "Finds a specific media item that matches given uuid.",
					nickname = "getMediaByUuid")
	@ApiResponses(
	{ //
			@ApiResponse(code = 404, message = "When no media is found matching the given uuid (MediaNotFoundException)."),
			@ApiResponse(code = 200, message = "Media data", response = MediaData.class) })
	public MediaData getMediaByUuid( //
			@ApiParam(value = "The universally unique identifier of the media item", required = true) //
			@PathVariable final String uuid)
	{
		final de.hybris.platform.cmsfacades.data.MediaData media = getMediaFacade().getMediaByUUID(uuid);

		return getDataMapper().map(media, MediaData.class);
	}

	@PostMapping(value = "/uuids")
	@ResponseBody
	@ApiOperation(value = "Gets a list of media by uuids.", notes = "Retrieves a list of media items that match the given uuids by POSTing the uuids in the request body",
					nickname = "getMediaByUuids")
	@ApiResponses(
	{ //
			@ApiResponse(code = 404, message = "When one of the media cannot be found (MediaNotFoundException)."),
			@ApiResponse(code = 200, message = "A list of media data", response = MediaListData.class) })
	public MediaListData getMediaByUuids( //
			@ApiParam(value = "List of uuids representing the media to retrieve", required = true) //
			@RequestBody final CMSItemUuidListWsDTO uuids)
	{
		final MediaListData mediaList = new MediaListData();
		final List<MediaData> mediaDataList = getDataMapper().mapAsList(getMediaFacade().getMediaByUUIDs(uuids.getUuids()),
				MediaData.class, null);
		mediaList.setMedia(mediaDataList);

		return mediaList;
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Gets media by query.", notes = "Gets a specific media item by named query.", nickname = "getMediaByQuery")
	@ApiResponses(
	{ //
			@ApiResponse(code = 400, message = "When the named query parameters provide contain validation errors"),
			@ApiResponse(code = 200, message = "A single page of query results as a list of media or an empty list (WebserviceValidationException).", response = MediaListData.class) })
	@ApiImplicitParams(
	{ //
			@ApiImplicitParam(name = "params",
					value = "The list of the filtering parameters for the namedQuery.\nEx:\"catalogId:catalogIdValue,catalogVersion:catalogVersionValue,code:codeValue\"",
					required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "currentPage", value = "The index of the requested page (index 0 means page 1).", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "pageSize", value = "The number of results per page.", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "namedQuery", value = "The name of the named query to use for the search.", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "sort", value = "The requested ordering for the search results.", required = true, dataType = "string", paramType = "query") })
	public MediaListData getMediaByQuery(//
			@ApiIgnore(value = "The name of the named query to use for the search.") //
			@ModelAttribute("namedQuery") final NamedQueryData namedQuery)
	{
		final MediaListData mediaList = new MediaListData();

		try
		{
			final de.hybris.platform.cmsfacades.data.NamedQueryData convertedNamedQuery = //
					getDataMapper().map(namedQuery, de.hybris.platform.cmsfacades.data.NamedQueryData.class);
			final List<MediaData> mediaDataList = getMediaFacade().getMediaByNamedQuery(convertedNamedQuery).stream()
					.map(media -> getDataMapper().map(media, MediaData.class)) //
					.collect(Collectors.toList());
			mediaList.setMedia(mediaDataList);
		}
		catch (final ValidationException e)
		{
			LOGGER.info("Validation exception", e);
			throw new WebserviceValidationException(e.getValidationObject());
		}
		return mediaList;
	}

	protected DataMapper getDataMapper()
	{
		return dataMapper;
	}

	public void setDataMapper(final DataMapper dataMapper)
	{
		this.dataMapper = dataMapper;
	}

	protected MediaFacade getMediaFacade()
	{
		return mediaFacade;
	}

	public void setMediaFacade(final MediaFacade mediaFacade)
	{
		this.mediaFacade = mediaFacade;
	}
}
