/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.webservicescommons.swagger.strategies.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;

import springfox.documentation.service.ObjectVendorExtension;
import springfox.documentation.service.StringVendorExtension;
import springfox.documentation.service.VendorExtension;

import static de.hybris.platform.webservicescommons.swagger.strategies.ConfigApiVendorExtensionStrategy.CONFIG_DELIMITER;
import static de.hybris.platform.webservicescommons.swagger.strategies.impl.DefaultApiVendorExtensionStrategy.CONFIG_API_TYPE;
import static de.hybris.platform.webservicescommons.swagger.strategies.impl.DefaultApiVendorExtensionStrategy.CONFIG_SHORT_TEXT;
import static de.hybris.platform.webservicescommons.swagger.strategies.impl.DefaultApiVendorExtensionStrategy.CONFIG_STATE;
import static de.hybris.platform.webservicescommons.swagger.strategies.impl.DefaultApiVendorExtensionStrategy.EXT_API_TYPE;
import static de.hybris.platform.webservicescommons.swagger.strategies.impl.DefaultApiVendorExtensionStrategy.EXT_SHORT_TEXT;
import static de.hybris.platform.webservicescommons.swagger.strategies.impl.DefaultApiVendorExtensionStrategy.EXT_STATE;
import static de.hybris.platform.webservicescommons.swagger.strategies.impl.DefaultApiVendorExtensionStrategy.EXT_STATE_INFO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Unit tests for {@link DefaultApiVendorExtensionStrategy}
 */
@UnitTest
public class DefaultApiVendorExtensionStrategyTest
{
	private static final String API_TYPE = "API Type";
	private static final String SHORT_TEXT = "Short Text";
	private static final String STATE = "State";
	private static final String CONFIG_PREFIX = "config.prefix";
	private static final int ALL_DEFAULT_VENDOR_EXTENSIONS_NUMBER = 3;
	private static final int EXT_API_TYPE_IDX = 0;
	private static final int EXT_SHORT_TEXT_IDX = 1;
	private static final int EXT_STATE_IDX = 2;
	private final DefaultApiVendorExtensionStrategy defaultApiVendorExtensionStrategy = new DefaultApiVendorExtensionStrategy();
	private Configuration configuration;

	@Before
	public void setup()
	{
		final ConfigurationService configurationService = mock(ConfigurationService.class);
		configuration = mock(Configuration.class);
		when(configurationService.getConfiguration()).thenReturn(configuration);
		defaultApiVendorExtensionStrategy.setConfigurationService(configurationService);
	}

	@Test
	public void getApiType()
	{
		//given
		when(configuration.getString(String.join(CONFIG_DELIMITER, CONFIG_PREFIX, CONFIG_API_TYPE))).thenReturn(API_TYPE);
		//when
		final String apiType = defaultApiVendorExtensionStrategy.getApiType(CONFIG_PREFIX);
		//then
		assertThat(apiType).isEqualTo(API_TYPE);
	}

	@Test
	public void getShortText()
	{
		//given
		when(configuration.getString(String.join(CONFIG_DELIMITER, CONFIG_PREFIX, CONFIG_SHORT_TEXT))).thenReturn(SHORT_TEXT);
		//when
		final String shortText = defaultApiVendorExtensionStrategy.getShortText(CONFIG_PREFIX);
		//then
		assertThat(shortText).isEqualTo(SHORT_TEXT);
	}

	@Test
	public void getState()
	{
		//given
		when(configuration.getString(String.join(CONFIG_DELIMITER, CONFIG_PREFIX, CONFIG_STATE))).thenReturn(STATE);
		//when
		final String state = defaultApiVendorExtensionStrategy.getState(CONFIG_PREFIX);
		//then
		assertThat(state).isEqualTo(STATE);
	}

	@Test
	public void getVendorExtensions()
	{
		//given
		when(configuration.getString(String.join(CONFIG_DELIMITER, CONFIG_PREFIX, CONFIG_API_TYPE))).thenReturn(API_TYPE);
		when(configuration.getString(String.join(CONFIG_DELIMITER, CONFIG_PREFIX, CONFIG_SHORT_TEXT))).thenReturn(SHORT_TEXT);
		when(configuration.getString(String.join(CONFIG_DELIMITER, CONFIG_PREFIX, CONFIG_STATE))).thenReturn(STATE);
		// when
		final List<VendorExtension> vendorExtensions = defaultApiVendorExtensionStrategy.getVendorExtensions(CONFIG_PREFIX);
		//then
		assertThat(vendorExtensions).hasSize(ALL_DEFAULT_VENDOR_EXTENSIONS_NUMBER);
		assertThat(vendorExtensions).element(EXT_API_TYPE_IDX).isExactlyInstanceOf(StringVendorExtension.class);
		assertThat(vendorExtensions).element(EXT_SHORT_TEXT_IDX).isExactlyInstanceOf(StringVendorExtension.class);
		assertThat(vendorExtensions).element(EXT_STATE_IDX).isExactlyInstanceOf(ObjectVendorExtension.class);
		assertThat(vendorExtensions.get(EXT_API_TYPE_IDX).getName()).isEqualTo(EXT_API_TYPE);
		assertThat(vendorExtensions.get(EXT_SHORT_TEXT_IDX).getName()).isEqualTo(EXT_SHORT_TEXT);
		assertThat(vendorExtensions.get(EXT_STATE_IDX).getName()).isEqualTo(EXT_STATE_INFO);
		final List<VendorExtension> stateInfoExtensions = ((ObjectVendorExtension) vendorExtensions.get(EXT_STATE_IDX)).getValue();
		assertThat(stateInfoExtensions).isNotEmpty();
		assertThat(stateInfoExtensions).element(0).isExactlyInstanceOf(StringVendorExtension.class);
		assertThat(stateInfoExtensions.get(0).getName()).isEqualTo(EXT_STATE);
	}
}
