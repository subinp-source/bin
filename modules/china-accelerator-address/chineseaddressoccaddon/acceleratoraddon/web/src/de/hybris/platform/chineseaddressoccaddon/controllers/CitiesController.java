/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseaddressoccaddon.controllers;

import de.hybris.platform.addressfacades.address.AddressFacade;
import de.hybris.platform.addressfacades.data.CityDataList;
import de.hybris.platform.addressfacades.data.DistrictDataList;
import de.hybris.platform.chinesecommercewebservicescommons.dto.CityListWsDTO;
import de.hybris.platform.chinesecommercewebservicescommons.dto.DistrictListWsDTO;
import de.hybris.platform.webservicescommons.cache.CacheControl;
import de.hybris.platform.webservicescommons.cache.CacheControlDirective;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * APIs for Chinese address.
 */
@Controller
@RequestMapping(value = "/{baseSiteId}")
@CacheControl(directive = CacheControlDirective.PRIVATE)
@Api(tags = "Cities")
public class CitiesController
{
	@Resource(name = "chineseAddressFacade")
	private AddressFacade chineseAddressFacade;

	@Resource(name = "dataMapper")
	private DataMapper dataMapper;

	@ResponseBody
	@RequestMapping(value = "/regions/{regionId}/cities", method = RequestMethod.GET)
	@ApiOperation(value = "Gets cities for a region.", notes = "Gets cities for a region.")
	@ApiBaseSiteIdParam
	public CityListWsDTO getCities(@ApiParam(value = "Region identifier", required = true) @PathVariable final String regionId)
	{
		final CityDataList cities = new CityDataList();
		cities.setCities(getChineseAddressFacade().getCitiesForRegion(regionId));

		return getDataMapper().map(cities, CityListWsDTO.class);
	}

	@ResponseBody
	@RequestMapping(value = "/cities/{cityId}/districts", method = RequestMethod.GET)
	@ApiOperation(value = "Gets districts for a city.", notes = "Gets districts for a city.")
	@ApiBaseSiteIdParam
	public DistrictListWsDTO getDistricts(@ApiParam(value = "City identifier", required = true) @PathVariable final String cityId)
	{
		final DistrictDataList districts = new DistrictDataList();
		districts.setDistricts(getChineseAddressFacade().getDistrictsForCity(cityId));

		return getDataMapper().map(districts, DistrictListWsDTO.class);
	}

	protected DataMapper getDataMapper()
	{
		return dataMapper;
	}

	public AddressFacade getChineseAddressFacade()
	{
		return chineseAddressFacade;
	}
}
