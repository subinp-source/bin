/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices.synchronization.controller;

import static de.hybris.platform.cmswebservices.constants.CmswebservicesConstants.API_VERSION;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cmsfacades.data.SyncRequestData;
import de.hybris.platform.cmsfacades.exception.ValidationException;
import de.hybris.platform.cmsfacades.synchronization.SynchronizationFacade;
import de.hybris.platform.cmswebservices.data.SyncJobData;
import de.hybris.platform.cmswebservices.data.SyncJobRequestData;
import de.hybris.platform.cmswebservices.security.IsAuthorizedCmsManager;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Api;
import springfox.documentation.annotations.ApiIgnore;

/*
 * Suppress sonar warning (squid:S1166 | Exception handlers should preserve the original exceptions) : It is
 * perfectly acceptable not to handle "e" here
 */
@SuppressWarnings("squid:S1166")
/**
 * Controller that handles synchronization of catalogs
 */
@Controller
@IsAuthorizedCmsManager
@RequestMapping(API_VERSION + "/catalogs/{catalogId}")
@Api(tags = "catalog version synchronization")
public class CatalogVersionSynchronizationController
{

	@Resource
	private SynchronizationFacade synchronizationFacade;
	@Resource
	private DataMapper dataMapper;

	@GetMapping(value = "/versions/{sourceVersionId}/synchronizations/versions/{targetVersionId}")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "Gets synchronization status.", notes = "Retrieves the status of the last synchronization for a catalog. Information is\n" +
			"retrieved based on a given catalog, source version and target version ids.",
					nickname = "getSynchronizationByCatalog")
	@ApiResponses(
	{ @ApiResponse(code = 200, message = "The synchronization status", response = SyncJobData.class) })
	@ApiImplicitParams(
	{ @ApiImplicitParam(name = "catalogId", value = "The catalog id", required = true, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "sourceVersionId", value = "Catalog version used as a starting point in this synchronization", required = true, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "targetVersionId", value = "Catalog version destination to be synchronized", required = true, dataType = "string", paramType = "path") })
	public SyncJobData getSynchronizationByCatalogSourceTarget(
			@ApiParam(value = "Contains the synchronization request data", required = true)
			@ModelAttribute
			final SyncJobRequestData syncJobRequest)
	{
		try
		{
			final SyncRequestData convertedSyncJobRequest = getDataMapper().map(syncJobRequest, SyncRequestData.class);

			final de.hybris.platform.cmsfacades.data.SyncJobData syncJobResult = getSynchronizationFacade()
					.getSynchronizationByCatalogSourceTarget(convertedSyncJobRequest);

			return getDataMapper().map(syncJobResult, SyncJobData.class);
		}
		catch (final ValidationException e)
		{
			throw new WebserviceValidationException(e.getValidationObject());
		}
	}

	/**
	 * @deprecated since 2005, please use {@link CatalogVersionSynchronizationController#requestNewSynchronizationByCatalogSourceTarget(SyncJobRequestData)} instead.
	 */
	@Deprecated(since = "2005", forRemoval = true)
	@PutMapping(value = "/versions/{sourceVersionId}/synchronizations/versions/{targetVersionId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiOperation(value = "Creates a catalog synchronization.", notes = "Generates a brand new synchronization status. The status is generated based on a given catalog, source version and target version ids.",
			nickname = "replaceSynchronizationByCatalog")
	@ApiResponses(
	{ @ApiResponse(code = 400, message = "When one of the catalogs does not exist (CMSItemNotFoundException)."),
			@ApiResponse(code = 200, message = "The synchronization status", response = SyncJobData.class) })
	@ApiImplicitParams(
	{ @ApiImplicitParam(name = "catalogId", value = "The catalog id", required = true, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "sourceVersionId", value = "Catalog version used as a starting point in this synchronization", required = true, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "targetVersionId", value = "Catalog version destination to be synchronized", required = true, dataType = "string", paramType = "path") })
	public SyncJobData createSynchronizationByCatalogSourceTarget(
			@ApiParam(value = "Contains the synchronization request data", required = true)
			@ModelAttribute
			final SyncJobRequestData syncJobRequest) throws CMSItemNotFoundException
	{
		return requestNewSynchronizationByCatalogSourceTarget(syncJobRequest);
	}

	@PostMapping(value = "/versions/{sourceVersionId}/synchronizations/versions/{targetVersionId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiOperation(value = "Creates a catalog synchronization.", notes = "Generates a brand new synchronization status. The status is generated based on a given catalog, source version and target version ids.",
					nickname = "createNewSynchronizationByCatalog")
	@ApiResponses(
			{ @ApiResponse(code = 400, message = "When one of the catalogs does not exist (CMSItemNotFoundException)."),
					@ApiResponse(code = 200, message = "The synchronization status", response = SyncJobData.class) })
	@ApiImplicitParams(
			{ @ApiImplicitParam(name = "catalogId", value = "The catalog id", required = true, dataType = "string", paramType = "path"),
					@ApiImplicitParam(name = "sourceVersionId", value = "Catalog version used as a starting point in this synchronization", required = true, dataType = "string", paramType = "path"),
					@ApiImplicitParam(name = "targetVersionId", value = "Catalog version destination to be synchronized", required = true, dataType = "string", paramType = "path") })
	public SyncJobData requestNewSynchronizationByCatalogSourceTarget(
			@ApiParam(value = "Contains the synchronization request data", required = true)
			@ModelAttribute
			final SyncJobRequestData syncJobRequest) throws CMSItemNotFoundException
	{
		try
		{
			final SyncRequestData convertedSyncJobRequest = getDataMapper().map(syncJobRequest, SyncRequestData.class);

			final de.hybris.platform.cmsfacades.data.SyncJobData syncJobResult = getSynchronizationFacade()
					.createCatalogSynchronization(convertedSyncJobRequest);

			return getDataMapper().map(syncJobResult, SyncJobData.class);
		}
		catch (final ValidationException e)
		{
			throw new WebserviceValidationException(e.getValidationObject());
		}
	}

	@GetMapping(value = "/synchronizations/targetversions/{targetVersionId}")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "Gets last synchronization by target catalog.", notes = "Retrieves the status of the last synchronization job. Information is retrieved based on the catalog version target.",
					nickname = "getLastSynchronizationByCatalog")
	@ApiResponses(
	{ @ApiResponse(code = 200, message = "The synchronization status", response = SyncJobData.class) })
	@ApiImplicitParams(
	{ @ApiImplicitParam(name = "catalogId", value = "The catalog id", required = true, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "targetVersionId", value = "Catalog version destination to be synchronized", required = true, dataType = "string", paramType = "path") })
	public SyncJobData getLastSynchronizationByCatalogTarget(
			@ApiIgnore(value = "Contains the synchronization request data")
			@ModelAttribute
			final SyncJobRequestData syncJobRequest)
	{
		try
		{
			final SyncRequestData convertedSyncJobRequest = getDataMapper().map(syncJobRequest, SyncRequestData.class);

			final de.hybris.platform.cmsfacades.data.SyncJobData syncJobResult = getSynchronizationFacade()
					.getLastSynchronizationByCatalogTarget(convertedSyncJobRequest);

			return getDataMapper().map(syncJobResult, SyncJobData.class);
		}
		catch (final ValidationException e)
		{
			throw new WebserviceValidationException(e.getValidationObject());
		}
	}

	public SynchronizationFacade getSynchronizationFacade()
	{
		return synchronizationFacade;
	}

	public void setSynchronizationFacade(final SynchronizationFacade synchronizationFacade)
	{
		this.synchronizationFacade = synchronizationFacade;
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
