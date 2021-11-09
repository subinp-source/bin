/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addressservices.strategies.impl;

import de.hybris.platform.addressservices.strategies.CellphoneValidateStrategy;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link CellphoneValidateStrategy}
 */
public class ChineseCellphoneValidateStrategy implements CellphoneValidateStrategy
{

	private ConfigurationService configurationService;
	private static final String CELLPHONE_VALIDATE_REGEX = "cellphone.validate.regex";
	private static final String DEFAULT_CELLPHONE_VALIDATE_REGEX = "^(?!\\s)(\\+86)?(\\s)?(\\d{11})$";

	@Override
	public boolean isInvalidCellphone(final String cellphone)
	{
		if (StringUtils.isBlank(cellphone))
		{
			return true;
		}

		final String regex = getConfigurationService().getConfiguration().getString(CELLPHONE_VALIDATE_REGEX,
				DEFAULT_CELLPHONE_VALIDATE_REGEX);
		return !Pattern.compile(regex).matcher(cellphone).matches();
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}


}
