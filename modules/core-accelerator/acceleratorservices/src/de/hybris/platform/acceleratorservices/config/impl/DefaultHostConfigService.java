/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.config.impl;

import de.hybris.platform.acceleratorservices.config.ConfigLookup;
import de.hybris.platform.acceleratorservices.config.HostConfigService;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.acceleratorservices.uiexperience.UiExperienceService;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


/**
 * Default implementation of the HostConfigService.
 * Uses the hybris config service to lookup host specific properties.
 */
public class DefaultHostConfigService implements HostConfigService
{
	private ConfigurationService configurationService;
	private UiExperienceService uiExperienceService;

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	protected UiExperienceService getUiExperienceService()
	{
		return uiExperienceService;
	}

	@Required
	public void setUiExperienceService(final UiExperienceService uiExperienceService)
	{
		this.uiExperienceService = uiExperienceService;
	}

	@Override
	public String getProperty(final String property, final String hostname)
	{
		Assert.notNull(property, "property must not be null");
		Assert.notNull(hostname, "hostname must not be null");

		final Configuration configuration = getConfigurationService().getConfiguration();
		final UiExperienceLevel uiExperienceLevel = getUiExperienceService().getUiExperienceLevel();
		String uiExpLevel = uiExperienceLevel != null ? "." + uiExperienceLevel.getCode() : StringUtils.EMPTY;
		String hostNameParam = "." + hostname;
		// Try the host and UiExperience
		// Try the host on its own
		// Try the UiExperience on its own
		// Fallback to the property key only
		return configuration.getString(property + hostNameParam + uiExpLevel, configuration.getString(property + hostNameParam,
				configuration.getString(property + uiExpLevel, configuration.getString(property, null))));
	}

	@Override
	public ConfigLookup getConfigForHost(final String hostname)
	{
		return new HostConfigLookup(this, hostname);
	}

	public class HostConfigLookup extends AbstractConfigLookup
	{
		private final HostConfigService hostConfigService;
		private final String hostname;

		public HostConfigLookup(final HostConfigService hostConfigService, final String hostname)
		{
			this.hostConfigService = hostConfigService;
			this.hostname = hostname;
		}

		@Override
		protected String getProperty(final String property)
		{
			return hostConfigService.getProperty(property, hostname);
		}
	}
}
