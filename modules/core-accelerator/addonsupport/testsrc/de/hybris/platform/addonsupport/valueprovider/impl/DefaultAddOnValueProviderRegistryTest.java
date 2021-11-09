/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addonsupport.valueprovider.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.addonsupport.valueprovider.AddOnValueProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Unit test for the {@link DefaultAddOnValueProviderRegistry} class.
 */
@UnitTest
public class DefaultAddOnValueProviderRegistryTest
{
	private DefaultAddOnValueProviderRegistry registry;

	@Before
	public void setUp() throws Exception
	{
		final AddOnValueProvider valueProvider = new DefaultAddOnValueProvider();
		final Map<String, AddOnValueProvider> valueProviders = new HashMap<>();

		valueProviders.put("myaddon", valueProvider);

		registry = new DefaultAddOnValueProviderRegistry();
		registry.setValueProviders(valueProviders);
	}

	@Test
	public void testShouldGetValueProvider()
	{
		final Optional<AddOnValueProvider> optional = registry.get("myaddon");
		Assert.assertTrue("Optional value provider was empty.", optional.isPresent());
	}

	@Test
	public void testShouldGetEmptyOptional()
	{
		final Optional<AddOnValueProvider> optional = registry.get("unknown");
		Assert.assertFalse("Optional value provider was not empty.", optional.isPresent());
	}

}
