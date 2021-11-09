/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices.pagesrestrictions.controller;

import static de.hybris.platform.cmswebservices.constants.CmswebservicesConstants.API_VERSION;

import de.hybris.platform.cmsfacades.pagetypesrestrictiontypes.PageTypeRestrictionTypeFacade;
import de.hybris.platform.cmswebservices.data.PageTypeRestrictionTypeData;
import de.hybris.platform.cmswebservices.data.PageTypeRestrictionTypeListData;
import de.hybris.platform.cmswebservices.security.IsAuthorizedCmsManager;
import de.hybris.platform.webservicescommons.mapping.DataMapper;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Api;

/**
 * Controller that provides an API to retrieve all pages types and their restrictions types.
 */
@Controller
@IsAuthorizedCmsManager
@RequestMapping(API_VERSION + "/pagetypesrestrictiontypes")
@Api(tags = "page type restrictions")
public class PageTypesRestrictionTypesController
{

	@Resource
	private PageTypeRestrictionTypeFacade pageTypesRestrictionTypesFacade;
	@Resource
	private DataMapper dataMapper;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	@ApiOperation(value = "Gets restriction types by page type.", notes = "Retrieves a list of available restrictions types for a given page type.",
					nickname = "getRestictionTypesByPageType")
	@ApiResponses(
	{ @ApiResponse(code = 200, message = "DTO which serves as a wrapper object that contains a list of PageRestrictionListData, never null.", response = PageTypeRestrictionTypeListData.class) })
	public PageTypeRestrictionTypeListData getRestrictionTypesByPageType()
	{
		final List<PageTypeRestrictionTypeData> convertedResults = getDataMapper().mapAsList(
				getPageTypesRestrictionTypesFacade().getRestrictionTypesForAllPageTypes(), PageTypeRestrictionTypeData.class, null);

		final PageTypeRestrictionTypeListData pageTypesRestrictionTypesList = new PageTypeRestrictionTypeListData();
		pageTypesRestrictionTypesList.setPageTypeRestrictionTypeList(convertedResults);
		return pageTypesRestrictionTypesList;
	}

	protected PageTypeRestrictionTypeFacade getPageTypesRestrictionTypesFacade()
	{
		return pageTypesRestrictionTypesFacade;
	}

	public void setPageTypesRestrictionTypesFacade(final PageTypeRestrictionTypeFacade pageTypesRestrictionTypesFacade)
	{
		this.pageTypesRestrictionTypesFacade = pageTypesRestrictionTypesFacade;
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
