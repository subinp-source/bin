/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addressservices.interceptors;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.addressservices.model.CityModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



/**
 * Integration test for {@link ChineseAddressPrepareInterceptor}
 */
@IntegrationTest
public class ChineseAddressPrepareInterceptorTest extends ServicelayerTransactionalTest
{

	private static final String TOWN = "town";
	private static final String CITY_CODE = "CD";
	private static final String CITY_NAME = "Chengdu";

	@Resource
	private ModelService modelService;

	private AddressModel address;

	@Before
	public void setup()
	{
		final CityModel city = modelService.create(CityModel.class);
		city.setIsocode(CITY_CODE);
		city.setName(CITY_NAME);

		address = modelService.create(AddressModel.class);
		address.setTown(TOWN);
		address.setCity(city);

		final CustomerModel customer = modelService.create(CustomerModel.class);
		customer.setUid("customer");
		modelService.save(customer);

		address.setOwner(customer);
		modelService.save(address);
	}

	@Test
	public void testInterceptor()
	{
		Assert.assertEquals(CITY_NAME, address.getTown());
	}
}
