/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsoccaddon.controllers;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cmsfacades.data.AbstractCMSComponentData;
import de.hybris.platform.cmsfacades.exception.ValidationException;
import de.hybris.platform.cmsfacades.items.ComponentItemFacade;
import de.hybris.platform.cmsoccaddon.data.ComponentIDListWsDTO;
import de.hybris.platform.cmsoccaddon.data.ComponentListWsDTO;
import de.hybris.platform.cmsoccaddon.data.ComponentWsDTO;
import de.hybris.platform.cmsoccaddon.jaxb.adapters.components.ComponentAdapterUtil.ComponentAdaptedData;
import de.hybris.platform.cmsoccaddon.jaxb.adapters.components.ComponentListWsDTOAdapter;
import de.hybris.platform.cmsoccaddon.jaxb.adapters.components.ComponentListWsDTOAdapter.ListAdaptedComponents;
import de.hybris.platform.cmsoccaddon.jaxb.adapters.components.ComponentWsDTOAdapter;
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

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * Default Controller for CMS Component. This controller is used for all CMS components that don\"t have a specific
 * controller to handle them.
 */
@Controller
@RequestMapping(value = "/{baseSiteId}/cms")
@CacheControl(directive = CacheControlDirective.PRIVATE)
@ApiVersion("v2")
@Api(tags = "Components")
public class ComponentController
{
	public static final String DEFAULT_CURRENT_PAGE = "0";
	public static final String DEFAULT_PAGE_SIZE = "10";
	private static final Logger LOGGER = LoggerFactory.getLogger(ComponentController.class);

	@Resource(name = "componentItemFacade")
	private ComponentItemFacade componentItemFacade;

	@Resource(name = "cmsDataMapper")
	private CMSDataMapper dataMapper;

	@Resource(name = "webPaginationUtils")
	private WebPaginationUtils webPaginationUtils;

	@GetMapping(value = "/components/{componentId}")
	@ResponseBody
	@ApiOperation(value = "Get component data by id", notes = "Given a component identifier, return cms component data.", nickname = "getComponentById")
	@ApiBaseSiteIdParam
	public ComponentAdaptedData getComponentById(
			@ApiParam(value = "Component identifier", required = true) @PathVariable final String componentId,
			@ApiParam(value = "Catalog code") @RequestParam(required = false) final String catalogCode,
			@ApiParam(value = "Product code") @RequestParam(required = false) final String productCode,
			@ApiParam(value = "Category code") @RequestParam(required = false) final String categoryCode,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = "DEFAULT") final String fields)
			throws CMSItemNotFoundException
	{
		try
		{
			final AbstractCMSComponentData componentData = getComponentItemFacade().getComponentById(componentId, categoryCode,
					productCode, catalogCode);
			final ComponentWsDTO data = (ComponentWsDTO) getDataMapper().map(componentData, fields);

			final ComponentWsDTOAdapter adapter = new ComponentWsDTOAdapter();
			return adapter.marshal(data);
		}
		catch (final ValidationException e)
		{
			LOGGER.info("Validation exception", e);
			throw new WebserviceValidationException(e.getValidationObject());
		}
	}

	/**
	 * This endpoint will trigger CORS preflight requests by the browser in a cross-origin setup, which is the most
	 * common cases. You can use the GET endpoint
	 * ({@link #findComponentsByIds(List, String, String, String, String, int, int, String)}) to easily circumvent the
	 * browser's preflight requests.
	 * <p>
	 * However, when requesting for a very large number of components using the GET endpoint, you may eventually reach
	 * the maximum number of characters allowed in a URL request. For example: <br>
	 * {@code Request Method: GET} <br>
	 * {@code Request URL: /cms/components?componentIds=uid1,uid2,uid3,...,uid1000&catalogCode=...}
	 * <p>
	 * This POST endpoint allows you to request for such large number of components, but you will have to address the
	 * preflight requests issues in a cross-origin setup.
	 *
	 * @deprecated since 1905, please use
	 *             {@link #findComponentsByIds(List, String, String, String, String, int, int, String)} instead.
	 */
	@PostMapping(value = "/components")
	@ResponseBody
	@ApiOperation(value = "Get components' data by id given in body", notes = "Given a list of component identifiers in body, return cms component data.", nickname = "searchComponentsByIds")
	@ApiBaseSiteIdParam
	@Deprecated(since = "1905", forRemoval = true)
	public ListAdaptedComponents searchComponentsByIds(
			@ApiParam(value = "List of Component identifiers", required = true) @RequestBody final ComponentIDListWsDTO componentIdList,
			@ApiParam(value = "Catalog code") @RequestParam(required = false) final String catalogCode,
			@ApiParam(value = "Product code") @RequestParam(required = false) final String productCode,
			@ApiParam(value = "Category code") @RequestParam(required = false) final String categoryCode,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = "DEFAULT") final String fields,
			@ApiParam(value = "Optional pagination parameter. Default value 0.") @RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@ApiParam(value = "Optional pagination parameter. Default value 10.") @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@ApiParam(value = "Optional sort criterion. No default value.") @RequestParam(required = false) final String sort)
	{
		// Validate for componentIdList, if componentIdList from body is null or empty, throw an IllegalArgumentException
		if (componentIdList == null || CollectionUtils.isEmpty(componentIdList.getIdList()))
		{
			throw new IllegalArgumentException("idList in the request body should contain component ID(s)");
		}

		return getComponentsByIds(componentIdList.getIdList(), catalogCode, productCode, categoryCode, fields, currentPage,
				pageSize, sort);
	}

	@GetMapping(value = "/components")
	@ResponseBody
	@ApiOperation(value = "Get component data", notes = "Finds cms components by the specified IDs. When none is provided, this will retrieve all components"
			+ "\nThe components list will be filtered by the given catalog, product or category restrictions, as well as by the "
			+ "pagination information. The result will be sorted in the specified order.", nickname = "getComponentsByIds")
	@ApiBaseSiteIdParam
	public ListAdaptedComponents findComponentsByIds( //
			@ApiParam(value = "List of Component identifiers") @RequestParam(required = false) final List<String> componentIds,
			@ApiParam(value = "Catalog code") @RequestParam(required = false) final String catalogCode,
			@ApiParam(value = "Product code") @RequestParam(required = false) final String productCode,
			@ApiParam(value = "Category code") @RequestParam(required = false) final String categoryCode,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = "DEFAULT") final String fields,
			@ApiParam(value = "Optional pagination parameter. Default value 0.") @RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@ApiParam(value = "Optional pagination parameter. Default value 10.") @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@ApiParam(value = "Optional sort criterion. No default value.") @RequestParam(required = false) final String sort)
	{
		return getComponentsByIds(componentIds, catalogCode, productCode, categoryCode, fields, currentPage, pageSize, sort);
	}

	/**
	 * Finds cms components by the specified IDs. When none is provided, this will retrieve all components. The
	 * components list will be filtered by the given catalog, product or category restrictions, as well as by the
	 * pagination information. The result will be sorted in the specified order.
	 *
	 * @param componentIds
	 *           - the list of component uid's
	 * @param catalogCode
	 *           - the catalog code determining the restriction to apply
	 * @param productCode
	 *           - the product code determining the restriction to apply
	 * @param categoryCode
	 *           - the category code determining the restriction to apply
	 * @param fields
	 *           - the response configuration determining which fields to include in the response
	 * @param currentPage
	 *           - the pagination information determining the page index
	 * @param pageSize
	 *           - the pagination information determining the page size
	 * @param sort
	 *           - the sorting information determining which field to sort on and the ordering direction (ASC or DESC)
	 * @return a list of cms component data
	 */
	protected ListAdaptedComponents getComponentsByIds(final List<String> componentIds, final String catalogCode,
			final String productCode, final String categoryCode, final String fields, final int currentPage, final int pageSize,
			final String sort)
	{
		try
		{
			// Creates a SearchPageData object contains requested pagination and sorting information
			final SearchPageData<AbstractCMSComponentData> searchPageDataInput = getWebPaginationUtils().buildSearchPageData(sort,
					currentPage, pageSize, true);

			// Get search result in a SearchPageData object contains search results with applied pagination and sorting information
			final SearchPageData<AbstractCMSComponentData> searchPageDataResult = getComponentItemFacade()
					.getComponentsByIds(componentIds, categoryCode, productCode, catalogCode, searchPageDataInput);

			return formatSearchPageDataResult(fields, searchPageDataResult);
		}
		catch (final ValidationException e)
		{
			LOGGER.info("Validation exception", e);
			throw new WebserviceValidationException(e.getValidationObject());
		}
	}

	/**
	 * Transforms the {@code SearchPageData} containing the list of cms component data into a
	 * {@code ListAdaptedComponents} object.
	 *
	 * @param fields
	 *           - the response configuration determining which fields to include in the response
	 * @param searchPageDataResult
	 *           - the configuration containing the pagination and sorting information
	 * @return a list of components of type {@link ListAdaptedComponents}
	 */
	protected ListAdaptedComponents formatSearchPageDataResult(final String fields,
			final SearchPageData<AbstractCMSComponentData> searchPageDataResult)
	{
		// Map the results into a ComponentListWsDTO which is an intermediate WsDTO
		final ComponentListWsDTO componentListWsDTO = new ComponentListWsDTO();
		final List<ComponentWsDTO> componentWsDTOList = getDataMapper().mapAsList(searchPageDataResult.getResults(),
				ComponentWsDTO.class, fields);
		componentListWsDTO.setComponent(componentWsDTOList);

		// Marshal the ComponentListWsDTO into ListAdaptedComponents
		final ComponentListWsDTOAdapter adapter = new ComponentListWsDTOAdapter();
		final ListAdaptedComponents listAdaptedComponent = adapter.marshal(componentListWsDTO);

		// Convert pagination and sorting data into ListAdaptedComponents' WsDTO
		final PaginationWsDTO paginationWsDTO = getWebPaginationUtils().buildPaginationWsDto(searchPageDataResult.getPagination());
		final List<SortWsDTO> sortWsDTOList = getWebPaginationUtils().buildSortWsDto(searchPageDataResult.getSorts());
		listAdaptedComponent.setPagination(paginationWsDTO);
		listAdaptedComponent.setSorts(sortWsDTOList);

		return listAdaptedComponent;
	}

	public ComponentItemFacade getComponentItemFacade()
	{
		return componentItemFacade;
	}

	public void setComponentItemFacade(final ComponentItemFacade componentItemFacade)
	{
		this.componentItemFacade = componentItemFacade;
	}

	public CMSDataMapper getDataMapper()
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
