/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsoccaddon.controllers;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cmsfacades.data.AbstractCMSComponentData;
import de.hybris.platform.cmsfacades.data.AbstractPageData;
import de.hybris.platform.cmsfacades.exception.ValidationException;
import de.hybris.platform.cmsfacades.pages.PageFacade;
import de.hybris.platform.cmsoccaddon.data.CMSPageListWsDTO;
import de.hybris.platform.cmsoccaddon.data.CMSPageWsDTO;
import de.hybris.platform.cmsoccaddon.jaxb.adapters.pages.PageAdapterUtil;
import de.hybris.platform.cmsoccaddon.jaxb.adapters.pages.PageListWsDTOAdapter;
import de.hybris.platform.cmsoccaddon.jaxb.adapters.pages.PageListWsDTOAdapter.ListAdaptedPages;
import de.hybris.platform.cmsoccaddon.jaxb.adapters.pages.PageWsDTOAdapter;
import de.hybris.platform.cmsoccaddon.mapping.CMSDataMapper;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.webservicescommons.cache.CacheControl;
import de.hybris.platform.webservicescommons.cache.CacheControlDirective;
import de.hybris.platform.webservicescommons.dto.PaginationWsDTO;
import de.hybris.platform.webservicescommons.dto.SortWsDTO;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.pagination.WebPaginationUtils;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * Controller to get cms page data with a list of content slots, each of which contains a list of cms component data
 */
@Controller
@RequestMapping(value = "/{baseSiteId}/cms")
@CacheControl(directive = CacheControlDirective.PRIVATE)
@ApiVersion("v2")
@Api(tags = "Pages")
public class PageController
{
	public static final String DEFAULT_CURRENT_PAGE = "0";
	public static final String DEFAULT_PAGE_SIZE = "10";

	@Resource(name = "cmsPageFacade")
	private PageFacade pageFacade;

	@Resource(name = "cmsDataMapper")
	protected CMSDataMapper dataMapper;

	@Resource(name = "webPaginationUtils")
	private WebPaginationUtils webPaginationUtils;

	private static final Logger LOGGER = LoggerFactory.getLogger(PageController.class);

	@GetMapping(value = "/pages")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiOperation(value = "Get page data with a list of cms content slots", //
			notes = "Get page data with a list of cms content slots based on pageLabelOrId or code. "
					+ "If none is provided then this will retrieve a Homepage cms content slots."
					+ "\nContent pages can be filtered by pageLabelOrId while the rest of the page types can be filtered by the code.", //
			nickname = "getPage")
	@ApiBaseSiteIdParam
	public PageAdapterUtil.PageAdaptedData getPage(
			@ApiParam(value = "Page type", allowableValues = "ContentPage, ProductPage, CategoryPage, CatalogPage") //
			@RequestParam(required = true, defaultValue = ContentPageModel._TYPECODE) final String pageType,
			@ApiParam(value = "Page Label or Id; if no page has a label that matches the provided label exactly, "
					+ "try to find pages whose label starts with a section of the provided label. Otherwise, try to find the page by id. "
					+ "Note: URL encoding on the page label should be applied when the label contains special characters") //
			@RequestParam(required = false) final String pageLabelOrId,
			@ApiParam(value = "If pageType is ProductPage, code should be product code; if pageType is CategoryPage, code should be category code; "
					+ "if pageType is CatalogPage, code should be catalog code") //
			@RequestParam(required = false) final String code,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") //
			@RequestParam(defaultValue = "DEFAULT") final String fields) throws CMSItemNotFoundException
	{
		try
		{
			final AbstractPageData pageData = getPageFacade().getPageData(pageType, pageLabelOrId, code);
			final CMSPageWsDTO page = (CMSPageWsDTO) getDataMapper().map(pageData, fields);

			final PageWsDTOAdapter adapter = new PageWsDTOAdapter();
			return adapter.marshal(page);
		}
		catch (final ValidationException e)
		{
			LOGGER.info("Validation exception", e);
			throw new WebserviceValidationException(e.getValidationObject());
		}
	}

	@GetMapping(value = "/pages/{pageId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiOperation(value = "Get page data for specific page id with a list of cms content slots", //
			notes = "Given a page identifier, return the page data with a list of cms content slots, each of which "
					+ "contains a list of cms component data.", //
			nickname = "getPageById")
	@ApiBaseSiteIdParam
	public PageAdapterUtil.PageAdaptedData getPage( //
			@ApiParam(value = "Page Id", required = true) //
			@PathVariable final String pageId,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") //
			@RequestParam(defaultValue = "DEFAULT") final String fields) throws CMSItemNotFoundException
	{
		try
		{
			final AbstractPageData pageData = getPageFacade().getPageData(pageId);
			final CMSPageWsDTO page = (CMSPageWsDTO) getDataMapper().map(pageData, fields);

			final PageWsDTOAdapter adapter = new PageWsDTOAdapter();
			return adapter.marshal(page);
		}
		catch (final ValidationException e)
		{
			LOGGER.info("Validation exception", e);
			throw new WebserviceValidationException(e.getValidationObject());
		}
	}

	@GetMapping(value = "/sitepages", params =
	{ "currentPage" })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiOperation(value = "Get a list of page data.", //
			notes = "Get a list page data with pagination support.", //
			nickname = "getAllPages")
	@ApiBaseSiteIdParam
	public ListAdaptedPages getAllPages( //
			@ApiParam(value = "Page type.", allowableValues = "ContentPage, ProductPage, CategoryPage, CatalogPage") //
			@RequestParam(required = false) final String pageType, //
			@ApiParam(value = "Pagination parameter. Default value 0.") //
			@RequestParam(required = true, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@ApiParam(value = "Optional pagination parameter. Default value 10.") //
			@RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@ApiParam(value = "Optional sort criterion. No default value.") //
			@RequestParam(required = false) final String sort,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") //
			@RequestParam(defaultValue = "DEFAULT") final String fields)
	{
		try
		{
			// Creates a SearchPageData object contains requested pagination and sorting information
			final SearchPageData<AbstractCMSComponentData> searchPageData = getWebPaginationUtils().buildSearchPageData(sort,
					currentPage, pageSize, true);
			final SearchPageData<AbstractPageData> searchResult = getPageFacade().findAllPageDataForType(pageType, searchPageData);
			return formatSearchPageDataResult(fields, searchResult);
		}
		catch (final ValidationException e)
		{
			LOGGER.info("Validation exception", e);
			throw new WebserviceValidationException(e.getValidationObject());
		}
	}

	/**
	 * Transforms the {@code SearchPageData} containing the list of page data into a {@code ListAdaptedPages} object.
	 *
	 * @param fields
	 *           - the response configuration determining which fields to include in the response
	 * @param searchResult
	 *           - the configuration containing the search results, pagination and sorting information
	 * @return a list of page of type {@link ListAdaptedPages}
	 */
	protected ListAdaptedPages formatSearchPageDataResult(final String fields, final SearchPageData<AbstractPageData> searchResult)
	{
		// Map the results into a CMSPageListWsDTO which is an intermediate WsDTO
		final CMSPageListWsDTO pageListWsDTO = new CMSPageListWsDTO();
		final List<CMSPageWsDTO> pageList = searchResult.getResults().stream()
				.map(pageData -> (CMSPageWsDTO) getDataMapper().map(pageData, fields)) //
				.collect(Collectors.toList());
		pageListWsDTO.setPage(pageList);

		// Marshal the CMSPageListWsDTO into ListAdaptedPages
		final PageListWsDTOAdapter adapter = new PageListWsDTOAdapter();
		final ListAdaptedPages listAdaptedPages = adapter.marshal(pageListWsDTO);

		// Convert pagination and sorting data into ListAdaptedPages' WsDTO
		final PaginationWsDTO paginationWsDTO = getWebPaginationUtils().buildPaginationWsDto(searchResult.getPagination());
		final List<SortWsDTO> sortWsDTOList = getWebPaginationUtils().buildSortWsDto(searchResult.getSorts());
		listAdaptedPages.setPagination(paginationWsDTO);
		listAdaptedPages.setSorts(sortWsDTOList);

		return listAdaptedPages;
	}

	protected PageFacade getPageFacade()
	{
		return pageFacade;
	}

	public void setPageFacade(final PageFacade pageFacade)
	{
		this.pageFacade = pageFacade;
	}

	protected CMSDataMapper getDataMapper()
	{
		return dataMapper;
	}

	public void setDataMapper(final CMSDataMapper dataMapper)
	{
		this.dataMapper = dataMapper;
	}

	public WebPaginationUtils getWebPaginationUtils()
	{
		return webPaginationUtils;
	}

	public void setWebPaginationUtils(final WebPaginationUtils webPaginationUtils)
	{
		this.webPaginationUtils = webPaginationUtils;
	}
}
