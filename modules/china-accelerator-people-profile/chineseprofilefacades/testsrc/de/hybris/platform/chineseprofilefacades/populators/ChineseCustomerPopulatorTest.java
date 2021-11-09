/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseprofilefacades.populators;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.chineseprofileservices.customer.ChineseCustomerAccountService;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.user.CustomerModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 *
 */
@UnitTest
public class ChineseCustomerPopulatorTest
{
	private static final String ISO_CODE_EN = "en";

	private static final String MOBILE_NUMBER = "13800138000";

	private CustomerModel source;

	private CustomerData target;

	private ChineseCustomerPopulator customerPopulator;

	@Mock
	private ChineseCustomerAccountService chineseCustomerAccountService;

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);

		source = new CustomerModel();
		source.setEmailLanguage(ISO_CODE_EN);
		source.setMobileNumber(MOBILE_NUMBER);

		target = new CustomerData();
		customerPopulator = new ChineseCustomerPopulator(chineseCustomerAccountService);
	}

	@Test
	public void testPopulate()
	{
		customerPopulator.populate(source, target);
		Assert.assertEquals(target.getEmailLanguage(), ISO_CODE_EN);
		Assert.assertEquals(target.getMobileNumber(), MOBILE_NUMBER);

		source.setEmailLanguage(null);
		Mockito.when(chineseCustomerAccountService.getDefaultEmailLanguage()).thenReturn(ISO_CODE_EN);
		customerPopulator.populate(source, target);
		Assert.assertEquals(target.getEmailLanguage(), ISO_CODE_EN);
	}
}
