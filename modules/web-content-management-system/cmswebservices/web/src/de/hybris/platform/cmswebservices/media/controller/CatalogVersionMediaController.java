/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices.media.controller;

import static de.hybris.platform.cmswebservices.constants.CmswebservicesConstants.API_VERSION;

import de.hybris.platform.cmsfacades.dto.MediaFileDto;
import de.hybris.platform.cmsfacades.exception.ValidationException;
import de.hybris.platform.cmsfacades.header.LocationHeaderResource;
import de.hybris.platform.cmsfacades.media.MediaFacade;
import de.hybris.platform.cmswebservices.constants.CmswebservicesConstants;
import de.hybris.platform.cmswebservices.data.MediaData;
import de.hybris.platform.cmswebservices.security.IsAuthorizedCmsManager;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;
import io.swagger.annotations.Api;

/**
 * Controller that provides media.
 */
@Controller
@IsAuthorizedCmsManager
@RequestMapping(API_VERSION + "/catalogs/{catalogId}/versions/{versionId}" + CatalogVersionMediaController.MEDIA_URI_PATH)
@Api(tags = "catalog version media")
public class CatalogVersionMediaController
{
	private static final Logger LOGGER = LoggerFactory.getLogger(CatalogVersionMediaController.class);
	public static final String MEDIA_URI_PATH = "/media";

	@Resource
	private MediaFacade mediaFacade;

	@Resource
	private LocationHeaderResource locationHeaderResource;

	@Resource
	private DataMapper dataMapper;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	@ApiOperation(value = "Uploads media.", notes = "Provides a new multipart media item for a given catalogId.", nickname = "doUploadMultipartMedia")
	@ApiResponses(
	{ //
			@ApiResponse(code = 400, message = "When an error occurs parsing the MultipartFile (IOException) or when the media query parameters provided contain validation errors (WebserviceValidationException)"),
			@ApiResponse(code = 200, message = "The newly created Media item", response = MediaData.class) })
	@ApiImplicitParams(
	{ //
			@ApiImplicitParam(name = "altText", value = "The alternative text to use for the newly created media.", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "code", value = "The code to use for the newly created media.", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "description", value = "The description to use for the newly created media.", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "mime", value = "Internet Media Type for the media file.", required = false, dataType = "string", paramType = "query")})
	public MediaData uploadMultipartMedia(
			@ApiParam(value = "The unique identifier of the catalog for which to link the new media.", required = true) //
			@PathVariable("catalogId")
			final String catalogId,
			@ApiParam(value = "The specific catalog version to which the new media will be associated to.", required = true) //
			@PathVariable("versionId")
			final String versionId,
			@ApiIgnore(value = "The MediaData containing the data for the associated media item to be created.") //
			@ModelAttribute("media")
			final MediaData media,
			@ApiParam(value = "The file representing the actual binary contents of the media to be created.", required = true) //
			@RequestParam("file")
			final MultipartFile multiPart, //
			final HttpServletRequest request, final HttpServletResponse response) throws IOException
	{
		media.setCatalogId(catalogId);
		media.setCatalogVersion(versionId);

		try
		{
			final de.hybris.platform.cmsfacades.data.MediaData convertedMediaData = //
					getDataMapper().map(media, de.hybris.platform.cmsfacades.data.MediaData.class);
			final de.hybris.platform.cmsfacades.data.MediaData newMedia = //
					getMediaFacade().addMedia(convertedMediaData, getFile(multiPart, multiPart.getInputStream()));

			response.addHeader(CmswebservicesConstants.HEADER_LOCATION,
					getLocationHeaderResource().createLocationForChildResource(request, newMedia.getCode()));
			return getDataMapper().map(newMedia, MediaData.class);
		}
		catch (final ValidationException e)
		{
			LOGGER.info("Validation exception", e);
			throw new WebserviceValidationException(e.getValidationObject());
		}
	}

	/**
	 * Create a new media file DTO from the {@code MultipartFile}.
	 *
	 * @param file
	 *           - a Spring {@code MultipartFile}
	 * @param inputStream
	 *           - an input stream used to read the file
	 * @return a media file DTO
	 */
	public MediaFileDto getFile(final MultipartFile file, final InputStream inputStream)
	{
		final MediaFileDto mediaFile = new MediaFileDto();
		mediaFile.setInputStream(inputStream);
		mediaFile.setName(file.getOriginalFilename());
		mediaFile.setSize(file.getSize());
		mediaFile.setMime(file.getContentType());
		return mediaFile;
	}

	protected MediaFacade getMediaFacade()
	{
		return mediaFacade;
	}

	public void setMediaFacade(final MediaFacade mediaFacade)
	{
		this.mediaFacade = mediaFacade;
	}

	protected LocationHeaderResource getLocationHeaderResource()
	{
		return locationHeaderResource;
	}

	public void setLocationHeaderResource(final LocationHeaderResource locationHeaderResource)
	{
		this.locationHeaderResource = locationHeaderResource;
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
