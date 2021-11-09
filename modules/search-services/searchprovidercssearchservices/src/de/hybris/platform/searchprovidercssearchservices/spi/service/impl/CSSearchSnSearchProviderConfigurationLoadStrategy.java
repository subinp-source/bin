/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchprovidercssearchservices.spi.service.impl;

import de.hybris.platform.searchprovidercssearchservices.model.CSSearchSnSearchProviderConfigurationModel;
import de.hybris.platform.searchprovidercssearchservices.spi.data.CSSearchSnSearchProviderConfiguration;
import de.hybris.platform.searchservices.spi.service.SnSearchProviderConfigurationLoadStrategy;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.springframework.beans.factory.annotation.Required;


/**
 * Load Strategy for the search core service search provider configuration.
 */
public class CSSearchSnSearchProviderConfigurationLoadStrategy implements
		SnSearchProviderConfigurationLoadStrategy<CSSearchSnSearchProviderConfigurationModel, CSSearchSnSearchProviderConfiguration>
{
	private Converter<CSSearchSnSearchProviderConfigurationModel, CSSearchSnSearchProviderConfiguration> cssearchSnSearchProviderConfigurationConverter;

	@Override
	public CSSearchSnSearchProviderConfiguration load(final CSSearchSnSearchProviderConfigurationModel searchProviderConfiguration)
	{
		return cssearchSnSearchProviderConfigurationConverter.convert(searchProviderConfiguration);
	}

	public Converter<CSSearchSnSearchProviderConfigurationModel, CSSearchSnSearchProviderConfiguration> getCssearchSnSearchProviderConfigurationConverter()
	{
		return cssearchSnSearchProviderConfigurationConverter;
	}

	@Required
	public void setCssearchSnSearchProviderConfigurationConverter(
			final Converter<CSSearchSnSearchProviderConfigurationModel, CSSearchSnSearchProviderConfiguration> cssearchSnSearchProviderConfigurationConverter)
	{
		this.cssearchSnSearchProviderConfigurationConverter = cssearchSnSearchProviderConfigurationConverter;
	}
}
