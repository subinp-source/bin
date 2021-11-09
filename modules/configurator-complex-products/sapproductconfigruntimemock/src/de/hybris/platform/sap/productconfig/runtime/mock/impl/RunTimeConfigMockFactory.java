/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.runtime.mock.impl;

import de.hybris.platform.sap.productconfig.runtime.mock.ConfigMock;
import de.hybris.platform.sap.productconfig.runtime.mock.ConfigMockFactory;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;


/**
 * Manages the creation of mock implementations for base product and variant product codes
 */
public class RunTimeConfigMockFactory implements ConfigMockFactory
{
	private CommonI18NService i18NService;

	@Override
	public ConfigMock createConfigMockForProductCode(final String productCode, final String variantProductCode)
	{
		ConfigMock mock = null;

		switch (productCode)
		{
			case "CPQ_HOME_THEATER":
				mock = new CPQHomeTheaterPocConfigMockImpl();
				break;

			case "CPQ_LAPTOP":
				mock = new CPQLaptopPocConfigMockImpl();
				break;

			case "CONF_BANDSAW_ML":
				mock = new CPQBandsawMockImpl();
				break;

			case "CONF_SCREWDRIVER_S":
				mock = new CPQScrewdriverMockImpl(variantProductCode);
				break;

			case "CONF_CAMERA_SL":
				mock = new DigitalCameraMockImpl(variantProductCode);
				break;

			case "CONF_HOME_THEATER_ML":
				mock = new HomeTheaterMockImpl();
				break;


			default:
				if (productCode.startsWith("CONF_PIPE"))
				{
					mock = createConfPipeMock(variantProductCode);
				}
				else
				{
					mock = new YSapSimplePocConfigMockImpl();
				}
				break;
		}
		mock.setI18NService(getI18NService());
		return mock;
	}

	protected ConfigMock createConfPipeMock(final String variantProductCode)
	{
		final ConfPipeMockImpl pipeMock = new ConfPipeMockImpl();
		pipeMock.setVariantCode(variantProductCode);
		return pipeMock;
	}

	@Override
	public ConfigMock createConfigMockForProductCode(final String productCode)
	{
		return createConfigMockForProductCode(productCode, null);
	}

	protected CommonI18NService getI18NService()
	{
		return i18NService;
	}

	/**
	 * @param i18nService
	 */
	public void setI18NService(final CommonI18NService i18nService)
	{
		i18NService = i18nService;
	}
}
