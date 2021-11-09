/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.mediaconversion.mock;

import java.util.Properties;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.MapConfiguration;

import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.PlatformConfig;
import de.hybris.platform.servicelayer.config.ConfigurationService;

/**
 * @author pohl
 */
public class MockConfigurationService implements ConfigurationService
{
	private final Configuration config;

	public MockConfigurationService()
	{
		final Properties props = new Properties();
		final PlatformConfig platformConfig = ConfigUtil.getPlatformConfig(this.getClass());
		ConfigUtil.loadRuntimeProperties(props, platformConfig);
		this.config = new MapConfiguration(props);
	}

	@Override
	public Configuration getConfiguration()
	{
		return this.config;
	}
}
