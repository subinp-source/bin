/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.webservicescommons.swagger.strategies;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.webservicescommons.swagger.strategies.impl.DefaultApiVendorExtensionStrategy;

import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;

import static de.hybris.platform.webservicescommons.swagger.strategies.ConfigApiVendorExtensionStrategy.CONFIG_DELIMITER;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Unit tests for {@link ConfigApiVendorExtensionStrategy}
 */
@UnitTest
public class ConfigApiVendorExtensionStrategyTest
{
	private static final String CONFIG_PREFIX = "config.prefix";
	private static final String CONFIG_KEY_PART1 = "first";
	private static final String CONFIG_KEY_PART2 = "second";
	private static final String CONFIG_KEY_SUFFIX = "config.key";
	private static final String CONFIG_VALUE = "configValue";

	private final ConfigApiVendorExtensionStrategy configApiVendorExtensionStrategy = new DefaultApiVendorExtensionStrategy();
	private Configuration configuration;

	@Before
	public void setup()
	{
		final ConfigurationService configurationService = mock(ConfigurationService.class);
		configuration = mock(Configuration.class);
		when(configurationService.getConfiguration()).thenReturn(configuration);
		configApiVendorExtensionStrategy.setConfigurationService(configurationService);
	}

	@Test
	public void getConfigKey()
	{
		//when
		final String configKey = configApiVendorExtensionStrategy.getConfigKey(CONFIG_PREFIX, CONFIG_KEY_PART1, CONFIG_KEY_SUFFIX);
		//then
		assertEquals(String.join(CONFIG_DELIMITER, CONFIG_PREFIX, CONFIG_KEY_PART1, CONFIG_KEY_SUFFIX), configKey);
	}

	@Test
	public void getConfigValue()
	{
		//given
		final String configKey = String
				.join(CONFIG_DELIMITER, CONFIG_PREFIX, CONFIG_KEY_PART1, CONFIG_KEY_PART2, CONFIG_KEY_SUFFIX);
		when(configuration.getString(configKey)).thenReturn(CONFIG_VALUE);
		//when
		final String serverConfigValue = configApiVendorExtensionStrategy
				.getConfigValue(CONFIG_PREFIX, CONFIG_KEY_PART1, CONFIG_KEY_PART2, CONFIG_KEY_SUFFIX);
		//then
		assertEquals(CONFIG_VALUE, serverConfigValue);
	}
}
