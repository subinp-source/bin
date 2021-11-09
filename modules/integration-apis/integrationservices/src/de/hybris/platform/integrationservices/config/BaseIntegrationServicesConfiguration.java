/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.integrationservices.config;

import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.NoSuchElementException;

import org.apache.commons.configuration.ConversionException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

/**
 * Provides common functionality for child classes
 */
public class BaseIntegrationServicesConfiguration
{
	private static final Logger LOG = Log.getLogger(BaseIntegrationServicesConfiguration.class);

	protected static final String FALLBACK_MESSAGE = "Property '{}' was not configured or not configured correctly. Using fallback value '{}'.";

	private ConfigurationService configurationService;

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	/**
	 * Gets the String property from the {@link ConfigurationService}.
	 * If there's an exception, the default value is returned
	 *
	 * @param property Property to get
	 * @param defaultValue Default value to return if an exception occurs
	 * @return The property value
	 */
	protected String getStringProperty(final String property, final String defaultValue)
	{
		try
		{
			final String value = getConfigurationService().getConfiguration().getString(property);
			if (StringUtils.isNotBlank(value))
			{
				return value;
			}
			LOG.warn(FALLBACK_MESSAGE, property, defaultValue);
		}
		catch(final NoSuchElementException | ConversionException e)
		{
			LOG.warn(FALLBACK_MESSAGE, property, defaultValue, e);
		}
		return defaultValue;
	}

	/**
	 * Gets the boolean property from the {@link ConfigurationService}.
	 * If there's an exception, the default value is returned
	 *
	 * @param property Property to get
	 * @param defaultValue Default value to return if an exception occurs
	 * @return The property value
	 */
	protected boolean getBooleanProperty(final String property, final boolean defaultValue)
	{
		try
		{
			return getConfigurationService().getConfiguration().getBoolean(property);
		}
		catch (final NoSuchElementException | ConversionException e)
		{
			LOG.warn(FALLBACK_MESSAGE, property, defaultValue, e);
			return defaultValue;
		}
	}

	/**
	 * Gets the integer property from the {@link ConfigurationService}.
	 * If there's an exception, the default value is returned
	 *
	 * @param property Property to get
	 * @param defaultValue Default value to return if an exception occurs
	 * @return The property value
	 */
	protected int getIntegerProperty(final String property, final int defaultValue)
	{
		try
		{
			return getConfigurationService().getConfiguration().getInt(property);
		}
		catch(final NoSuchElementException | ConversionException e)
		{
			LOG.warn(FALLBACK_MESSAGE, property, defaultValue, e);
			return defaultValue;
		}
	}

	/**
	 * Gets the long value property from the {@link ConfigurationService}.
	 * If there's an exception, the default value is returned
	 *
	 * @param property Property to get
	 * @param defaultValue Default value to return if an exception occurs
	 * @return The property value
	 */
	protected long getLongProperty(final String property, final long defaultValue)
	{
		try
		{
			return getConfigurationService().getConfiguration().getLong(property);
		}
		catch(final NoSuchElementException | ConversionException e)
		{
			LOG.warn(FALLBACK_MESSAGE, property, defaultValue, e);
			return defaultValue;
		}
	}

	/**
	 * Sets the property from the {@link ConfigurationService}
	 *
	 * @param property Property to set
	 * @param value Value that is set on the property
	 */
	protected void setProperty(final String property, final String value){
		getConfigurationService().getConfiguration().setProperty(property, value);
	}

	/**
	 * Sets the property from the {@link ConfigurationService}
	 *
	 * @param property Property to set
	 * @param value int value that is set on the property
	 */
	protected void setProperty(final String property, final int value){
		setProperty(property, String.valueOf(value));
	}

	/**
	 * Sets the property from the {@link ConfigurationService}
	 *
	 * @param property Property to set
	 * @param value long value that is set on the property
	 */
	protected void setProperty(final String property, final long value){
		setProperty(property, String.valueOf(value));
	}
}
