/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.webservicescommons.swagger.strategies.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;

import springfox.documentation.service.VendorExtension;

import static de.hybris.platform.webservicescommons.swagger.strategies.ConfigApiVendorExtensionStrategy.CONFIG_ARRAY_DELIMITER;
import static de.hybris.platform.webservicescommons.swagger.strategies.ConfigApiVendorExtensionStrategy.CONFIG_DELIMITER;
import static de.hybris.platform.webservicescommons.swagger.strategies.impl.SecurityApiVendorExtensionStrategy.CONFIG_SCOPES;
import static de.hybris.platform.webservicescommons.swagger.strategies.impl.SecurityApiVendorExtensionStrategy.CONFIG_SECURITY;
import static de.hybris.platform.webservicescommons.swagger.strategies.impl.SecurityApiVendorExtensionStrategy.CONFIG_SECURITY_NAMES;
import static de.hybris.platform.webservicescommons.swagger.strategies.impl.SecurityApiVendorExtensionStrategy.SECURITY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Unit tests for {@link SecurityApiVendorExtensionStrategy}
 */
@UnitTest
public class SecurityApiVendorExtensionStrategyTest
{
	private static final String SECURITY_NAME_1 = "security1";
	private static final String SECURITY_NAME_2 = "security2";
	private static final String SCOPE_NAME_1 = "scope1";
	private static final String SCOPE_NAME_2 = "scope2";
	private static final String CONFIG_PREFIX = "config.prefix";
	private final SecurityApiVendorExtensionStrategy securityApiVendorExtensionStrategy = new SecurityApiVendorExtensionStrategy();
	private Configuration configuration;

	@Before
	public void setup()
	{
		final ConfigurationService configurationService = mock(ConfigurationService.class);
		configuration = mock(Configuration.class);
		when(configurationService.getConfiguration()).thenReturn(configuration);
		securityApiVendorExtensionStrategy.setConfigurationService(configurationService);
	}

	@Test
	public void getSecurityNames()
	{
		when(configuration.getString(String.join(CONFIG_DELIMITER, CONFIG_PREFIX, CONFIG_SECURITY_NAMES)))
				.thenReturn(String.join(CONFIG_ARRAY_DELIMITER, SECURITY_NAME_1, SECURITY_NAME_2));
		final String[] securityNames = securityApiVendorExtensionStrategy.getSecurityNames(CONFIG_PREFIX);
		assertEquals(2, securityNames.length);
		assertEquals(SECURITY_NAME_1, securityNames[0]);
		assertEquals(SECURITY_NAME_2, securityNames[1]);
	}

	@Test
	public void getSecurity()
	{
		//given
		when(configuration.getString(String.join(CONFIG_DELIMITER, CONFIG_PREFIX, CONFIG_SECURITY_NAMES)))
				.thenReturn(String.join(CONFIG_ARRAY_DELIMITER, SECURITY_NAME_1, SECURITY_NAME_2));
		when(configuration.getString(String.join(CONFIG_DELIMITER, CONFIG_PREFIX, CONFIG_SECURITY, SECURITY_NAME_1, CONFIG_SCOPES)))
				.thenReturn(String.join(CONFIG_ARRAY_DELIMITER, SCOPE_NAME_1, SCOPE_NAME_2));
		when(configuration.getString(String.join(CONFIG_DELIMITER, CONFIG_PREFIX, CONFIG_SECURITY, SECURITY_NAME_2, CONFIG_SCOPES)))
				.thenReturn(String.join(CONFIG_ARRAY_DELIMITER, SCOPE_NAME_1, SCOPE_NAME_2));
		//when
		final List<Map<String, List<String>>> securityList = securityApiVendorExtensionStrategy.getSecurity(CONFIG_PREFIX);
		//then
		assertNotNull(securityList);
		assertEquals(2, securityList.size());
		assertNotNull(securityList.get(0).get(SECURITY_NAME_1));
		assertNotNull(securityList.get(1).get(SECURITY_NAME_2));
		assertEquals(2, securityList.get(0).get(SECURITY_NAME_1).size());
		assertEquals(2, securityList.get(1).get(SECURITY_NAME_2).size());
		final List<String> scopeList1 = securityList.get(0).get(SECURITY_NAME_1);
		assertEquals(SCOPE_NAME_1, scopeList1.get(0));
		assertEquals(SCOPE_NAME_2, scopeList1.get(1));
		final List<String> scopeList2 = securityList.get(1).get(SECURITY_NAME_2);
		assertEquals(SCOPE_NAME_1, scopeList2.get(0));
		assertEquals(SCOPE_NAME_2, scopeList2.get(1));
	}

	@Test
	public void getSecurityScopes()
	{
		//given
		when(configuration.getString(String.join(CONFIG_DELIMITER, CONFIG_PREFIX, CONFIG_SECURITY, SECURITY_NAME_1, CONFIG_SCOPES)))
				.thenReturn(String.join(CONFIG_ARRAY_DELIMITER, SCOPE_NAME_1, SCOPE_NAME_2));
		//when
		final List<String> securityScopes = securityApiVendorExtensionStrategy.getSecurityScopes(CONFIG_PREFIX, SECURITY_NAME_1);
		//then
		assertNotNull(securityScopes);
		assertEquals(2, securityScopes.size());
		assertEquals(SCOPE_NAME_1, securityScopes.get(0));
		assertEquals(SCOPE_NAME_2, securityScopes.get(1));
	}

	@Test
	public void getVendorExtensions()
	{
		//given
		when(configuration.getString(String.join(CONFIG_DELIMITER, CONFIG_PREFIX, CONFIG_SECURITY_NAMES)))
				.thenReturn(SECURITY_NAME_1);
		when(configuration.getString(String.join(CONFIG_DELIMITER, CONFIG_PREFIX, CONFIG_SECURITY, SECURITY_NAME_1, CONFIG_SCOPES)))
				.thenReturn(SCOPE_NAME_1);
		//when
		final List<VendorExtension> vendorExtensions = securityApiVendorExtensionStrategy.getVendorExtensions(CONFIG_PREFIX);
		//then
		assertNotNull(vendorExtensions);
		assertEquals(1, vendorExtensions.size());
		assertEquals(SECURITY, vendorExtensions.get(0).getName());
	}
}
