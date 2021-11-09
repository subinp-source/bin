/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.webservicescommons.swagger.strategies.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;

import springfox.documentation.service.VendorExtension;

import static de.hybris.platform.webservicescommons.swagger.strategies.ConfigApiVendorExtensionStrategy.CONFIG_ARRAY_DELIMITER;
import static de.hybris.platform.webservicescommons.swagger.strategies.ConfigApiVendorExtensionStrategy.CONFIG_DELIMITER;
import static de.hybris.platform.webservicescommons.swagger.strategies.impl.ServerApiVendorExtensionStrategy.CONFIG_DEFAULT;
import static de.hybris.platform.webservicescommons.swagger.strategies.impl.ServerApiVendorExtensionStrategy.CONFIG_ENUM;
import static de.hybris.platform.webservicescommons.swagger.strategies.impl.ServerApiVendorExtensionStrategy.CONFIG_SERVER;
import static de.hybris.platform.webservicescommons.swagger.strategies.impl.ServerApiVendorExtensionStrategy.CONFIG_SERVERS;
import static de.hybris.platform.webservicescommons.swagger.strategies.impl.ServerApiVendorExtensionStrategy.CONFIG_SERVER_DESCRIPTION;
import static de.hybris.platform.webservicescommons.swagger.strategies.impl.ServerApiVendorExtensionStrategy.CONFIG_SERVER_URL;
import static de.hybris.platform.webservicescommons.swagger.strategies.impl.ServerApiVendorExtensionStrategy.CONFIG_TEMPLATES;
import static de.hybris.platform.webservicescommons.swagger.strategies.impl.ServerApiVendorExtensionStrategy.EXT_SERVERS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Unit tests for {@link ServerApiVendorExtensionStrategy}
 */
@UnitTest
public class ServerApiVendorExtensionStrategyTest
{
	private static final String RELATIVE_SERVER_CONFIG_VALUE = "relativeServerConfigValue";
	private static final String SERVER_NAME = "serverName";
	private static final String URL_PLACEHOLDER = "url";
	private static final String RELATIVE_SERVER_TEMPLATES_CONFIG_VALUE = "relativeServerTemplatesConfigValue";
	private static final String SERVER_NAME_1 = "server1";
	private static final String SERVER_NAME_2 = "server2";
	private static final String URL_DESCRIPTION = "URL description";
	private static final String URL_DESCRIPTION_2 = "URL description 2";
	private static final String URL_PLACEHOLDER_2 = "apiType";
	private static final String URL_WITH_ONE_PLACEHOLDER = "https://{url}/rest/v2";
	private static final String URL_WITH_TWO_PLACEHOLDERS = "https://{url}/{apiType}/v2";
	private static final String EMPTY_STRING = "";
	private static final String DEFAULT_VALUE = "default_value";
	private static final String ENUM_VALUE_1 = "enum_value_1";
	private static final String ENUM_VALUE_2 = "enum_value_2";
	private static final String CONFIG_PREFIX = "config.prefix";
	private static final String RELATIVE_CONFIG_KEY = "relative.config.key";
	private final ServerApiVendorExtensionStrategy serverApiVendorExtensionStrategy = new ServerApiVendorExtensionStrategy();
	private Configuration configuration;

	@Before
	public void setup()
	{
		final ConfigurationService configurationService = mock(ConfigurationService.class);
		configuration = mock(Configuration.class);
		when(configurationService.getConfiguration()).thenReturn(configuration);
		serverApiVendorExtensionStrategy.setConfigurationService(configurationService);
	}

	@Test
	public void getServerConfigValue()
	{
		//given
		when(configuration.getString(String.join(CONFIG_DELIMITER, CONFIG_PREFIX, CONFIG_SERVER, SERVER_NAME, RELATIVE_CONFIG_KEY)))
				.thenReturn(RELATIVE_SERVER_CONFIG_VALUE);
		//when
		final String serverConfigValue = serverApiVendorExtensionStrategy
				.getServerConfigValue(CONFIG_PREFIX, SERVER_NAME, RELATIVE_CONFIG_KEY);
		//then
		assertEquals(RELATIVE_SERVER_CONFIG_VALUE, serverConfigValue);
	}

	@Test
	public void getServerTemplatesConfigValue()
	{
		//given
		final String configKey = String.join(CONFIG_DELIMITER, CONFIG_PREFIX, CONFIG_SERVER, SERVER_NAME, CONFIG_TEMPLATES, //
				URL_PLACEHOLDER, RELATIVE_CONFIG_KEY);
		when(configuration.getString(configKey)).thenReturn(RELATIVE_SERVER_TEMPLATES_CONFIG_VALUE);
		//when
		final String serverConfigValue = serverApiVendorExtensionStrategy
				.getServerTemplatesConfigValue(CONFIG_PREFIX, SERVER_NAME, URL_PLACEHOLDER, RELATIVE_CONFIG_KEY);
		//then
		assertEquals(RELATIVE_SERVER_TEMPLATES_CONFIG_VALUE, serverConfigValue);
	}

	@Test
	public void getServersNames()
	{
		when(configuration.getString(String.join(CONFIG_DELIMITER, CONFIG_PREFIX, CONFIG_SERVERS)))
				.thenReturn(String.join(CONFIG_ARRAY_DELIMITER, SERVER_NAME_1, SERVER_NAME_2));
		final String[] serversNames = serverApiVendorExtensionStrategy.getServersNames(CONFIG_PREFIX);
		assertEquals(2, serversNames.length);
		assertEquals(SERVER_NAME_1, serversNames[0]);
		assertEquals(SERVER_NAME_2, serversNames[1]);
	}

	@Test
	public void getTemplatesForOneUrlPlaceholder()
	{
		//given
		final String configKey = String.join(CONFIG_DELIMITER, CONFIG_PREFIX, CONFIG_SERVER, SERVER_NAME, CONFIG_TEMPLATES, //
				URL_PLACEHOLDER, CONFIG_SERVER_DESCRIPTION);
		when(configuration.getString(configKey)).thenReturn(URL_DESCRIPTION);
		//when
		final Map<String, Object> templates = serverApiVendorExtensionStrategy
				.getTemplates(CONFIG_PREFIX, SERVER_NAME, URL_WITH_ONE_PLACEHOLDER);
		//then
		assertEquals(1, templates.size());
		assertEquals(URL_DESCRIPTION, ((Map<String, Object>) templates.get(URL_PLACEHOLDER)).get(CONFIG_SERVER_DESCRIPTION));
	}

	@Test
	public void getTemplatesForOneUrlPlaceholderWithoutConfigProperty()
	{
		//given
		final String configKey = String.join(CONFIG_DELIMITER, CONFIG_PREFIX, CONFIG_SERVER, SERVER_NAME, CONFIG_TEMPLATES, //
				URL_PLACEHOLDER, CONFIG_SERVER_DESCRIPTION);
		when(configuration.getString(configKey)).thenReturn(EMPTY_STRING);
		//when
		final Map<String, Object> templates = serverApiVendorExtensionStrategy
				.getTemplates(CONFIG_PREFIX, SERVER_NAME, URL_WITH_ONE_PLACEHOLDER);
		//then
		assertEquals(1, templates.size());
		assertEquals(EMPTY_STRING, ((Map<String, Object>) templates.get(URL_PLACEHOLDER)).get(CONFIG_SERVER_DESCRIPTION));
	}

	@Test
	public void getTemplatesForTwoUrlPlaceholder()
	{
		//given
		String configKey = String.join(CONFIG_DELIMITER, CONFIG_PREFIX, CONFIG_SERVER, SERVER_NAME, CONFIG_TEMPLATES, //
				URL_PLACEHOLDER, CONFIG_SERVER_DESCRIPTION);
		when(configuration.getString(configKey)).thenReturn(URL_DESCRIPTION);
		configKey = String.join(CONFIG_DELIMITER, CONFIG_PREFIX, CONFIG_SERVER, SERVER_NAME, CONFIG_TEMPLATES, URL_PLACEHOLDER_2,
				CONFIG_SERVER_DESCRIPTION);
		when(configuration.getString(configKey)).thenReturn(URL_DESCRIPTION_2);
		//when
		final Map<String, Object> templates = serverApiVendorExtensionStrategy
				.getTemplates(CONFIG_PREFIX, SERVER_NAME, URL_WITH_TWO_PLACEHOLDERS);
		//then
		assertEquals(2, templates.size());
		assertEquals(URL_DESCRIPTION, ((Map<String, Object>) templates.get(URL_PLACEHOLDER)).get(CONFIG_SERVER_DESCRIPTION));
		assertEquals(URL_DESCRIPTION_2, ((Map<String, Object>) templates.get(URL_PLACEHOLDER_2)).get(CONFIG_SERVER_DESCRIPTION));
	}

	@Test
	public void addTemplateEnumToMap()
	{
		//given
		final Map<String, Object> templateEntries = new HashMap<>();
		final String configKey = String.join(CONFIG_DELIMITER, CONFIG_PREFIX, CONFIG_SERVER, SERVER_NAME, CONFIG_TEMPLATES, //
				URL_PLACEHOLDER, CONFIG_ENUM);
		when(configuration.getString(configKey)).thenReturn(String.join(CONFIG_ARRAY_DELIMITER, ENUM_VALUE_1, ENUM_VALUE_2));
		//when
		serverApiVendorExtensionStrategy.addTemplateEnumToMap(templateEntries, CONFIG_PREFIX, SERVER_NAME, URL_PLACEHOLDER);
		//then
		assertEquals(1, templateEntries.size());
		assertEquals(2, ((List) templateEntries.get(CONFIG_ENUM)).size());
		assertEquals(ENUM_VALUE_1, ((List<String>) templateEntries.get(CONFIG_ENUM)).get(0));
		assertEquals(ENUM_VALUE_2, ((List<String>) templateEntries.get(CONFIG_ENUM)).get(1));
	}

	@Test
	public void addTemplateEmptyEnumToMap()
	{
		//given
		final Map<String, Object> templateEntries = new HashMap<>();
		final String configKey = String.join(CONFIG_DELIMITER, CONFIG_PREFIX, CONFIG_SERVER, SERVER_NAME, CONFIG_TEMPLATES, //
				URL_PLACEHOLDER, CONFIG_ENUM);
		when(configuration.getString(configKey)).thenReturn(EMPTY_STRING);
		//when
		serverApiVendorExtensionStrategy.addTemplateEnumToMap(templateEntries, CONFIG_PREFIX, SERVER_NAME, URL_PLACEHOLDER);
		//then
		assertEquals(0, templateEntries.size());
	}

	@Test
	public void addTemplateEmptyDefaultToMap()
	{
		//given
		final Map<String, Object> templateEntries = new HashMap<>();
		final String configKey = String.join(CONFIG_DELIMITER, CONFIG_PREFIX, CONFIG_SERVER, SERVER_NAME, CONFIG_TEMPLATES, //
				URL_PLACEHOLDER, CONFIG_DEFAULT);
		when(configuration.getString(configKey)).thenReturn(EMPTY_STRING);
		//when
		serverApiVendorExtensionStrategy.addTemplateDefaultToMap(templateEntries, CONFIG_PREFIX, SERVER_NAME, URL_PLACEHOLDER);
		//then
		assertEquals(0, templateEntries.size());
	}

	@Test
	public void addTemplateDefaultToMap()
	{
		//given
		final Map<String, Object> templateEntries = new HashMap<>();
		final String configKey = String.join(CONFIG_DELIMITER, CONFIG_PREFIX, CONFIG_SERVER, SERVER_NAME, CONFIG_TEMPLATES, //
				URL_PLACEHOLDER, CONFIG_DEFAULT);
		when(configuration.getString(configKey)).thenReturn(DEFAULT_VALUE);
		//when
		serverApiVendorExtensionStrategy.addTemplateDefaultToMap(templateEntries, CONFIG_PREFIX, SERVER_NAME, URL_PLACEHOLDER);
		//then
		assertEquals(1, templateEntries.size());
		assertEquals(DEFAULT_VALUE, templateEntries.get(CONFIG_DEFAULT));
	}

	@Test
	public void addTemplateDescriptionToMap()
	{
		//given
		final Map<String, Object> templateEntries = new HashMap<>();
		final String configKey = String.join(CONFIG_DELIMITER, CONFIG_PREFIX, CONFIG_SERVER, SERVER_NAME, CONFIG_TEMPLATES, //
				URL_PLACEHOLDER, CONFIG_SERVER_DESCRIPTION);
		when(configuration.getString(configKey)).thenReturn(URL_DESCRIPTION);
		//when
		serverApiVendorExtensionStrategy.addTemplateDescriptionToMap(templateEntries, CONFIG_PREFIX, SERVER_NAME, URL_PLACEHOLDER);
		//then
		assertEquals(1, templateEntries.size());
		assertEquals(URL_DESCRIPTION, templateEntries.get(CONFIG_SERVER_DESCRIPTION));
	}

	@Test
	public void getVendorExtensions()
	{
		//given
		when(configuration.getString(String.join(CONFIG_DELIMITER, CONFIG_PREFIX, CONFIG_SERVERS))).thenReturn(SERVER_NAME_1);
		when(configuration.getString(String.join(CONFIG_DELIMITER, CONFIG_PREFIX, CONFIG_SERVER, SERVER_NAME_1, CONFIG_SERVER_URL)))
				.thenReturn(URL_WITH_ONE_PLACEHOLDER);
		//when
		final List<VendorExtension> vendorExtensions = serverApiVendorExtensionStrategy.getVendorExtensions(CONFIG_PREFIX);
		//then
		assertNotNull(vendorExtensions);
		assertEquals(1, vendorExtensions.size());
		assertEquals(EXT_SERVERS, vendorExtensions.get(0).getName());
	}
}
