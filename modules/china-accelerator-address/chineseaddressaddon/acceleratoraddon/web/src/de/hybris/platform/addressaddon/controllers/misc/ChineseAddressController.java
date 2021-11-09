/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addressaddon.controllers.misc;


import de.hybris.platform.addressfacades.address.AddressFacade;
import de.hybris.platform.addressfacades.data.CityData;
import de.hybris.platform.addressfacades.data.DistrictData;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 *
 */
@Controller
@Scope("tenant")
@RequestMapping("/cn-address")
public class ChineseAddressController
{
	@Resource(name = "chineseAddressFacade")
	private AddressFacade chineseAddressFacade;

	@RequestMapping(value = "/region/{code}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<CityData> getRegionAddressForm(@PathVariable("code") final String region)
	{
		return chineseAddressFacade.getCitiesForRegion(region);
	}

	@RequestMapping(value = "/city/{code}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<DistrictData> getCityAddressForm(@PathVariable("code") final String city)
	{
		return chineseAddressFacade.getDistrictsForCity(city);
	}

}
