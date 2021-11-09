/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices.catalogversions.controller;

import static de.hybris.platform.cmswebservices.constants.CmswebservicesConstants.API_VERSION;
import static de.hybris.platform.cmswebservices.constants.CmswebservicesConstants.MODE_CLONEABLE_TO;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import de.hybris.platform.cmsfacades.catalogversions.CatalogVersionFacade;
import de.hybris.platform.cmsfacades.data.CatalogVersionData;
import de.hybris.platform.cmswebservices.data.CatalogVersionListData;
import de.hybris.platform.cmswebservices.security.IsAuthorizedCmsManager;
import de.hybris.platform.webservicescommons.mapping.DataMapper;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Api;

/**
/**
 * Controller that provides an API to retrieve a catalog version information.
 */
@RestController
@IsAuthorizedCmsManager
@RequestMapping(API_VERSION + "/sites/{siteId}/catalogs/{catalogId}/versions")
@Api(tags = {"catalog versions"})
class CatalogVersionController
{
	@Resource
	private CatalogVersionFacade catalogVersionFacade;

	@Resource
	private DataMapper dataMapper;

	@RequestMapping(value = "/{versionId}/targets", method = GET, params =
	{ "mode" })
	@ApiOperation( //
			value = "Gets writable content catalog versions.", notes = "Retrieves a target content catalog versions by mode. For mode = \"cloneableTo\" "
					+ "returns the list of content catalog versions (which are used as targets for page clone operations) "
					+ "for a given catalog or all child catalogs.", nickname = "getWritableContentCatalogVersion")
	@ApiImplicitParams(
	{ @ApiImplicitParam(name = "siteId", value = "The site identifier", required = true, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "catalogId", value = "The catalog id", required = true, dataType = "string", paramType = "path") })
	@ApiResponses(
	{ @ApiResponse(code = 400, message = "When the catalog and/or version specified is not valid (CMSItemNotFoundException)."),
			@ApiResponse(code = 200, message = "The list of content catalog versions.") })
	public CatalogVersionListData getWritableContentCatalogVersionsStartWith(
			@ApiParam(value = "The catalog identifier", required = true)
			@PathVariable("catalogId")
			final String catalogId, @ApiParam(value = "The site identifier", required = true)
			@PathVariable("siteId")
			final String siteId, @ApiParam(value = "The version of the catalog", required = true)
			@PathVariable("versionId")
			final String versionId, @ApiParam(value = "The mode to filter the result", required = true, example = MODE_CLONEABLE_TO)
			@RequestParam(value = "mode")
			final String mode)
	{
		final CatalogVersionListData listData = new CatalogVersionListData();
		if (mode.equals(MODE_CLONEABLE_TO))
		{
			final List<CatalogVersionData> catalogVersionDataList = getCatalogVersionFacade()
					.getWritableContentCatalogVersionTargets(siteId, catalogId, versionId);
			listData.setVersions(getDataMapper().mapAsList(catalogVersionDataList, CatalogVersionData.class, null));
		}
		else
		{
			listData.setVersions(new ArrayList<>());
		}
		return listData;
	}

	protected CatalogVersionFacade getCatalogVersionFacade()
	{
		return catalogVersionFacade;
	}

	public void setCatalogVersionFacade(final CatalogVersionFacade catalogVersionFacade)
	{
		this.catalogVersionFacade = catalogVersionFacade;
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
