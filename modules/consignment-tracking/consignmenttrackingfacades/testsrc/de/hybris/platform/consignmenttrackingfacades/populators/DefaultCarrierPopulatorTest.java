/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.consignmenttrackingfacades.populators;

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.consignmenttrackingfacades.delivery.data.CarrierData;
import de.hybris.platform.consignmenttrackingservices.model.CarrierModel;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultCarrierPopulatorTest
{

	@Mock
	private I18NService i18NService;

	private DefaultCarrierPopulator populator;

	private CarrierModel source;

	private CarrierData target;

	private Locale locale;

	@Before
	public void prepare()
	{

		MockitoAnnotations.initMocks(this);

		source = new CarrierModel();
		target = new CarrierData();
		locale = new Locale("en");

		populator = new DefaultCarrierPopulator();
		populator.setI18NService(i18NService);
	}

	@Test
	public void test_populate()
	{
		source.setCode("mock carrier");
		source.setName("Mock Carrier", locale);

		given(i18NService.getCurrentLocale()).willReturn(locale);
		populator.populate(source, target);

		Assert.assertEquals("mock carrier", target.getCode());
		Assert.assertEquals("Mock Carrier", target.getName());

	}

}
