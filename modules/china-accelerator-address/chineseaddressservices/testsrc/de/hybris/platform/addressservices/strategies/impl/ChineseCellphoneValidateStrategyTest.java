/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addressservices.strategies.impl;

import static org.mockito.Mockito.doReturn;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import org.apache.commons.configuration.Configuration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class ChineseCellphoneValidateStrategyTest
{
	@Mock
	private ConfigurationService configurationService;
	@Mock
	private Configuration configuration;
	private ChineseCellphoneValidateStrategy cellPhoneValidateStrategy;
	private String regex;
	private static final String CELLPHONE_VALIDATE_REGEX = "cellphone.validate.regex";
	private static final String DEFAULT_CELLPHONE_VALIDATE_REGEX = "^(?!\\s)(\\+86)?(\\s)?(\\d{11})$";

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);

		cellPhoneValidateStrategy = new ChineseCellphoneValidateStrategy();
		regex = "^(?!\\s)(\\+86)?(\\s)?(\\d{11})$";
		doReturn(configuration).when(configurationService).getConfiguration();
		cellPhoneValidateStrategy.setConfigurationService(configurationService);
	}

	@Test
	public void testIsInvalidCellphoneByValidCellphone()
	{
		doReturn(regex).when(configuration).getString(CELLPHONE_VALIDATE_REGEX, DEFAULT_CELLPHONE_VALIDATE_REGEX);

		final String cellphone = "18108065028";
		final boolean isInvalidCellphone = cellPhoneValidateStrategy.isInvalidCellphone(cellphone);
		Assert.assertFalse(isInvalidCellphone);

		final String cellphone2 = "+8618100000000";
		final boolean isInvalidCellphone2 = cellPhoneValidateStrategy.isInvalidCellphone(cellphone2);
		Assert.assertFalse(isInvalidCellphone2);

		final String cellphone3 = "+86 18100000000";
		final boolean isInvalidCellphone3 = cellPhoneValidateStrategy.isInvalidCellphone(cellphone3);
		Assert.assertFalse(isInvalidCellphone3);
	}

	@Test
	public void testIsInvalidCellphoneByUnValidCellphone()
	{
		doReturn(regex).when(configuration).getString(CELLPHONE_VALIDATE_REGEX, DEFAULT_CELLPHONE_VALIDATE_REGEX);

		final String cellphone = "1810000000a";
		final boolean isInvalidCellphone = cellPhoneValidateStrategy.isInvalidCellphone(cellphone);
		Assert.assertTrue(isInvalidCellphone);

		final String cellphone2 = "+4318100000000";
		final boolean isInvalidCellphone2 = cellPhoneValidateStrategy.isInvalidCellphone(cellphone2);
		Assert.assertTrue(isInvalidCellphone2);

		final String cellphone3 = "+86  18100000000";
		final boolean isInvalidCellphone3 = cellPhoneValidateStrategy.isInvalidCellphone(cellphone3);
		Assert.assertTrue(isInvalidCellphone3);

		final String cellphone4 = "+86 18100000000 ";
		final boolean isInvalidCellphone4 = cellPhoneValidateStrategy.isInvalidCellphone(cellphone4);
		Assert.assertTrue(isInvalidCellphone4);

		final String cellphone5 = " 18100000000";
		final boolean isInvalidCellphone5 = cellPhoneValidateStrategy.isInvalidCellphone(cellphone5);
		Assert.assertTrue(isInvalidCellphone5);
	}

	@Test
	public void testIsInvalidCellphoneByEmptyCellhone()
	{
		doReturn(regex).when(configuration).getString(CELLPHONE_VALIDATE_REGEX, DEFAULT_CELLPHONE_VALIDATE_REGEX);

		final String cellphone = "";
		final boolean isInvalidCellphone = cellPhoneValidateStrategy.isInvalidCellphone(cellphone);
		Assert.assertTrue(isInvalidCellphone);
	}


}
