/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices.navigations.controller;

import static de.hybris.platform.cmswebservices.constants.CmswebservicesConstants.API_VERSION;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cmsfacades.exception.ValidationException;
import de.hybris.platform.cmsfacades.header.LocationHeaderResource;
import de.hybris.platform.cmsfacades.navigations.NavigationFacade;
import de.hybris.platform.cmswebservices.constants.CmswebservicesConstants;
import de.hybris.platform.cmswebservices.data.NavigationNodeData;
import de.hybris.platform.cmswebservices.data.NavigationNodeListData;
import de.hybris.platform.cmswebservices.dto.CMSItemSearchWsDTO;
import de.hybris.platform.cmswebservices.dto.PageableWsDTO;
import de.hybris.platform.cmswebservices.security.IsAuthorizedCmsManager;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Api;

/**
 * Controller to deal with Navigation Nodes objects
 */
@Controller
@IsAuthorizedCmsManager
@RequestMapping(API_VERSION + "/sites/{siteId}/catalogs/{catalogId}/versions/{versionId}/navigations")
@Api(tags = "navigation")
public class NavigationController
{
	private static final Logger LOGGER = Logger.getLogger(NavigationController.class.getName());

	@Resource
	private NavigationFacade navigationFacade;

	@Resource
	private LocationHeaderResource locationHeaderResource;

	@Resource
	private DataMapper dataMapper;

	@RequestMapping(method = RequestMethod.GET, params =
	{ "parentUid" })
	@ResponseBody
	@ApiOperation(value = "Finds all navigation nodes.", notes = "Retrieves a list of available navigation nodes that match provided parentUid.",
					nickname = "getNavigationNodesByParentUId")
	@ApiImplicitParams(
	{ @ApiImplicitParam(name = "siteId", value = "The site identifier", required = true, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "catalogId", value = "The catalog name", required = true, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "versionId", value = "The catalog version identifier", required = true, dataType = "string", paramType = "path") })
	@ApiResponses(
	{ @ApiResponse(code = 200, message = "List of navigation nodes", response = NavigationNodeListData.class) })
	public NavigationNodeListData findAllNavigationNodesByParentUid(
			@ApiParam(value = "The parent navigation node Uid", required = true)
			@RequestParam(value = "parentUid")
			final String parentUid)
	{
		final List<NavigationNodeData> navigationNodes = getDataMapper()
				.mapAsList(getNavigationFacade().findAllNavigationNodes(parentUid), NavigationNodeData.class, null);

		final NavigationNodeListData navigationNodeListData = new NavigationNodeListData();
		navigationNodeListData.setNavigationNodes(navigationNodes);
		return navigationNodeListData;
	}

	/**
	 * @deprecated since 1811, please use
	 *             {@link de.hybris.platform.cmswebservices.cmsitems.controller.CMSItemController#findCmsItems(CMSItemSearchWsDTO, PageableWsDTO)}
	 *             instead.
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Finds all navigation nodes.", notes = "Retrieves a list of all available navigation nodes.", nickname = "getAllNavigationNodes")
	@ApiImplicitParams(
	{ @ApiImplicitParam(name = "siteId", value = "The site identifier", required = true, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "catalogId", value = "The catalog name", required = true, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "versionId", value = "The catalog version identifier", required = true, dataType = "string", paramType = "path") })
	@ApiResponses(
	{ @ApiResponse(code = 200, message = "List of all navigation nodes, excluding the super root.", response = NavigationNodeListData.class) })
	@Deprecated(since = "1811", forRemoval = true)
	public NavigationNodeListData findAllNavigationNodes()
	{
		final List<NavigationNodeData> navigationNodes = getDataMapper().mapAsList(getNavigationFacade().findAllNavigationNodes(),
				NavigationNodeData.class, null);

		final NavigationNodeListData navigationNodeListData = new NavigationNodeListData();
		navigationNodeListData.setNavigationNodes(navigationNodes);
		return navigationNodeListData;
	}

	/**
	 * @deprecated since 1811, please use
	 *             {@link de.hybris.platform.cmswebservices.cmsitems.controller.CMSItemController#getCMSItemByUUid(String)}
	 *             instead.
	 */
	@RequestMapping(value = "/{uid}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Finds navigation node.", notes = "Retrieves a specific navigation node item. Information is retrieved based on a given site and catalog ids.", nickname = "getNavigationNodeById")
	@ApiImplicitParams(
	{ @ApiImplicitParam(name = "siteId", value = "The site identifier", required = true, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "catalogId", value = "The catalog name", required = true, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "versionId", value = "The catalog version identifier", required = true, dataType = "string", paramType = "path") })
	@ApiResponses(
	{ @ApiResponse(code = 400, message = "When the Navigation Node has not been found (CMSItemNotFoundException)."),
			@ApiResponse(code = 200, message = "The navigation node identified by uid.", response = NavigationNodeData.class) })
	@Deprecated(since = "1811", forRemoval = true)
	public NavigationNodeData findNavigationNodeById(@ApiParam(value = "The navigation node's unique identifier", required = true)
	@PathVariable("uid")
	final String uid) throws CMSItemNotFoundException
	{
		return getDataMapper().map(getNavigationFacade().findNavigationNodeById(uid), NavigationNodeData.class);
	}

	/**
	 * @deprecated since 1811, please use
	 *             {@link de.hybris.platform.cmswebservices.cmsitems.controller.CMSItemController#updateCMSItem(String, Map)}
	 *             instead.
	 */
	@Deprecated(since = "1811", forRemoval = true)
	@RequestMapping(value = "/{uid}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiOperation(value = "Updates navigation node.", notes = "Provides new information to the navigation node for a given."
			+ "navigation node uid. Deprecated since 1811, please use CMSItemController controller instead.",
			nickname = "replaceNavigationNodeById")
	@ApiImplicitParams(
	{ @ApiImplicitParam(name = "siteId", value = "The site identifier", required = true, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "catalogId", value = "The catalog name", required = true, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "versionId", value = "The catalog version identifier", required = true, dataType = "string", paramType = "path") })
	@ApiResponses(
	{ @ApiResponse(code = 400, message = "When the Navigation Node has not been found (CMSItemNotFoundException) or if there is any validation error (WebserviceValidationException).") })
	public NavigationNodeData updateNavigationNodeById(@ApiParam(value = "The navigation node uid to be updated.", required = true)
	@PathVariable("uid")
	final String uid, @ApiParam(value = "The navigation node data", required = true)
	@RequestBody
	final NavigationNodeData navigationNode) throws CMSItemNotFoundException
	{
		try
		{
			final de.hybris.platform.cmsfacades.data.NavigationNodeData convertedNavigationNode = getDataMapper().map(navigationNode,
					de.hybris.platform.cmsfacades.data.NavigationNodeData.class);
			final de.hybris.platform.cmsfacades.data.NavigationNodeData updatedNavigationNode = getNavigationFacade()
					.updateNavigationNode(uid, convertedNavigationNode);
			return getDataMapper().map(updatedNavigationNode, NavigationNodeData.class);
		}
		catch (final ValidationException e)
		{
			LOGGER.debug(e);
			throw new WebserviceValidationException(e.getValidationObject());
		}
	}

	/**
	 * @deprecated since 1811, please use
	 *             {@link de.hybris.platform.cmswebservices.cmsitems.controller.CMSItemController#removeCMSItembyUUid(String)}
	 *             instead.
	 */
	@Deprecated(since = "1811", forRemoval = true)
	@RequestMapping(value = "/{uid}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Deletes navigation node.", notes = "Removes a specific instance of the navigation node that matches a given site id. Deprecated since 1811, please use "
			+ "DELETE /v1/sites/{siteId}/cmsitems/{uuid} instead.", nickname = "removeNavigationNodeById")
	@ApiImplicitParams(
	{ @ApiImplicitParam(name = "siteId", value = "The site identifier", required = true, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "catalogId", value = "The catalog name", required = true, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "versionId", value = "The catalog version identifier", required = true, dataType = "string", paramType = "path") })
	@ApiResponses(
	{

			@ApiResponse(code = 400, message = "When the Navigation Node has not been found (CMSItemNotFoundException).") })
	public void deleteNavigationNodeById(@ApiParam(value = "The navigation node's uid.", required = true)
	@PathVariable("uid")
	final String uid) throws CMSItemNotFoundException
	{
		getNavigationFacade().deleteNavigationNode(uid);
	}

	/**
	 * @deprecated since 1811, please use
	 *             {@link de.hybris.platform.cmswebservices.cmsitems.controller.CMSItemController#createCMSItem(Map, HttpServletRequest, HttpServletResponse)}
	 *             instead.
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Creates navigation node.", notes = "Adds a new navigation node for a given catalog id.",
					nickname = "createNavigationNodeById")
	@ApiImplicitParams(
	{ @ApiImplicitParam(name = "siteId", value = "The site identifier", required = true, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "catalogId", value = "The catalog name", required = true, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "versionId", value = "The catalog version identifier", required = true, dataType = "string", paramType = "path") })
	@ApiResponses(
	{ @ApiResponse(code = 400, message = "If the parentUid does not exist (CMSItemNotFoundException) or if there is any validation error (WebserviceValidationException)"),
			@ApiResponse(code = 200, message = "The navigation node data created.", response = NavigationNodeData.class) })
	@Deprecated(since = "1811", forRemoval = true)
	public NavigationNodeData createNavigationNodeById(@ApiParam(value = "The navigation node data to be added.", required = true)
	@RequestBody
	final NavigationNodeData navigationNode, final HttpServletRequest request, final HttpServletResponse response)
			throws CMSItemNotFoundException
	{
		try
		{
			final de.hybris.platform.cmsfacades.data.NavigationNodeData convertedNavigationNode = getDataMapper().map(navigationNode,
					de.hybris.platform.cmsfacades.data.NavigationNodeData.class);
			final de.hybris.platform.cmsfacades.data.NavigationNodeData responseData = getNavigationFacade()
					.addNavigationNode(convertedNavigationNode);

			response.addHeader(CmswebservicesConstants.HEADER_LOCATION,
					getLocationHeaderResource().createLocationForChildResource(request, responseData.getUid()));

			return getDataMapper().map(responseData, NavigationNodeData.class);
		}
		catch (final ValidationException e)
		{
			LOGGER.debug(e);
			throw new WebserviceValidationException(e.getValidationObject());
		}
	}

	@RequestMapping(method = RequestMethod.GET, params =
	{ "ancestorTrailFrom" })
	@ResponseBody
	@ApiOperation(value = "Finds all navigation ancestors and self.", notes = "Finds all navigation ancestors for a given node uid and the node itself.",
					nickname = "getNavigationAncestorsAndSelf")
	@ApiImplicitParams(
	{ @ApiImplicitParam(name = "siteId", value = "The site identifier", required = true, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "catalogId", value = "The catalog name", required = true, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "versionId", value = "The catalog version identifier", required = true, dataType = "string", paramType = "path") })
	@ApiResponses(
	{ @ApiResponse(code = 400, message = "When the navigation node uid does not exist (CMSItemNotFoundException)."),
			@ApiResponse(code = 200, message = "The navigation node's ancestors and the node itself.", response = NavigationNodeListData.class) })
	public NavigationNodeListData findAllNavigationAncestorsAndSelf(
			@ApiParam(value = "The navigation node's Uid whose ancestors are to be extracted.", required = true)
			@RequestParam(value = "ancestorTrailFrom")
			final String navigationNodeUid) throws CMSItemNotFoundException
	{
		final List<NavigationNodeData> navigationNodes = getDataMapper()
				.mapAsList(getNavigationFacade().getNavigationAncestorsAndSelf(navigationNodeUid), NavigationNodeData.class, null);

		final NavigationNodeListData navigationAncestorListData = new NavigationNodeListData();
		navigationAncestorListData.setNavigationNodes(navigationNodes);
		return navigationAncestorListData;
	}

	protected NavigationFacade getNavigationFacade()
	{
		return navigationFacade;
	}

	public void setNavigationFacade(final NavigationFacade navigationFacade)
	{
		this.navigationFacade = navigationFacade;
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
