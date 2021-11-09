/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmssmarteditwebservices.pages.controller;

import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cmsfacades.data.CMSComponentTypesForPageSearchData;
import de.hybris.platform.cmsfacades.exception.ValidationException;
import de.hybris.platform.cmsfacades.types.ComponentTypeFacade;
import de.hybris.platform.cmssmarteditwebservices.data.ComponentTypeData;
import de.hybris.platform.cmssmarteditwebservices.dto.CMSComponentTypeListWsDTO;
import de.hybris.platform.cmssmarteditwebservices.dto.CMSComponentTypesForPageSearchWsDTO;
import de.hybris.platform.cmssmarteditwebservices.dto.PageableWsDTO;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.mapping.config.FieldSetLevelMapping;
import de.hybris.platform.webservicescommons.pagination.WebPaginationUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static de.hybris.platform.cmssmarteditwebservices.constants.CmssmarteditwebservicesConstants.API_VERSION;
import static de.hybris.platform.cmssmarteditwebservices.constants.CmssmarteditwebservicesConstants.DEFAULT_CURRENT_PAGE;

/**
 * Controller that provides an API to retrieve information about component types applicable to the current page.
 */
@RestController
@RequestMapping(API_VERSION + "/catalogs/{catalogId}/versions/{versionId}/pages/{pageId}/types")
@Api(tags = "page component types")
public class PageComponentTypesController
{
    @Resource
    private ComponentTypeFacade componentTypeFacade;

    @Resource
    private DataMapper dataMapper;

    @Resource
    private WebPaginationUtils webPaginationUtils;

    @Resource
    private FieldSetLevelMapping cmsComponentTypeDataMapping;

    @GetMapping(params = { "pageSize", "currentPage" })
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ApiOperation(value = "Retrieves component types.", notes = "Retrieves the component types that can be added to a page.", nickname = "getComponentTypesAvailableToPage")
    @ApiResponses({
            @ApiResponse(code = 400, message = "If the given page cannot be found (CMSItemNotFoundException)."),
            @ApiResponse(code = 200, message = "The dto containing the list of component types applicable to a page. The results are paged.")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "catalogId", value = "The catalog id", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "versionId", value = "The uid of the catalog version", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "pageId", value = "The uid of the page for which to find its valid component types", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "pageSize", value = "Page size for paging", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "currentPage", value = "The requested page number", defaultValue = DEFAULT_CURRENT_PAGE, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "mask", value = "Search mask applied to the type code and name fields, Uses partial matching", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "readOnly", value = "Read only mode for attributes. Is only used if attributes are returned (FULL fields option is used) ", defaultValue = "false", paramType = "query")
    })
    public CMSComponentTypeListWsDTO getComponentTypesAvailableToPage(
            @ApiParam(value = "Component Types For Page Search DTO", required = true) //
            @ModelAttribute //
            final CMSComponentTypesForPageSearchWsDTO componentTypesForPageSearchWsDTO, //
            @ApiParam(value = "Pageable DTO", required = true) //
            @ModelAttribute //
            final PageableWsDTO pageableInfo, //
            @ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") //
            @RequestParam(defaultValue = "DEFAULT") //
            final String fields
            )
            throws CMSItemNotFoundException
    {
        try
        {
            final PageableData pageableData = getDataMapper().map(pageableInfo, PageableData.class);
            final CMSComponentTypesForPageSearchData searchData = getDataMapper().map(componentTypesForPageSearchWsDTO, CMSComponentTypesForPageSearchData.class);
            searchData.setRequiredFields(getRequiredFields(fields));

            return buildReply(getComponentTypeFacade().getAllComponentTypesForPage(searchData, pageableData), fields);
        }
        catch (final ValidationException e)
        {
            throw new WebserviceValidationException(e.getValidationObject());
        }
    }

    /**
     * Gets the list of fields which should be returned in the response.
     *
     * @param fields
     *         - A string whose value can be BASIC, DEFAULT, or FULL. The value of this string is mapped to the
     *         list of fields which must be returned in the response.
     * @return the list of fields which must be returned in the response.
     */
    protected List<String> getRequiredFields(final String fields)
    {
        return Arrays.stream(cmsComponentTypeDataMapping.getLevelMapping().getOrDefault(fields, "").split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    protected CMSComponentTypeListWsDTO buildReply(final SearchResult<de.hybris.platform.cmsfacades.data.ComponentTypeData> searchResult, final String fields)
    {
        final List<ComponentTypeData> resultList = getDataMapper().mapAsList(searchResult.getResult(), ComponentTypeData.class, fields);

        CMSComponentTypeListWsDTO componentTypeList = new CMSComponentTypeListWsDTO();
        componentTypeList.setComponentTypes(resultList);
        componentTypeList.setPagination(getWebPaginationUtils().buildPagination(searchResult));

        return componentTypeList;
    }

    public DataMapper getDataMapper()
    {
        return dataMapper;
    }

    public ComponentTypeFacade getComponentTypeFacade()
    {
        return componentTypeFacade;
    }

    public WebPaginationUtils getWebPaginationUtils()
    {
        return webPaginationUtils;
    }
}
