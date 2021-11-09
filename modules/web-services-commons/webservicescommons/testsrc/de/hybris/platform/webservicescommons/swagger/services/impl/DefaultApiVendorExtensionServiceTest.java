/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.webservicescommons.swagger.services.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.webservicescommons.swagger.strategies.ApiVendorExtensionStrategy;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import springfox.documentation.service.VendorExtension;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Unit tests for {@link DefaultApiVendorExtensionService}
 */
@UnitTest
public class DefaultApiVendorExtensionServiceTest
{

	private static final String CONFIG_PREFIX = "config.prefix";
	private final DefaultApiVendorExtensionService defaultApiVendorExtensionService = new DefaultApiVendorExtensionService();
	private ApiVendorExtensionStrategy apiVendorExtensionStrategy1;
	private ApiVendorExtensionStrategy apiVendorExtensionStrategy2;

	@Before
	public void setup()
	{
		final VendorExtension ext1 = mock(VendorExtension.class);
		final VendorExtension ext2 = mock(VendorExtension.class);
		final VendorExtension ext3 = mock(VendorExtension.class);
		apiVendorExtensionStrategy1 = mock(ApiVendorExtensionStrategy.class);
		when(apiVendorExtensionStrategy1.getVendorExtensions(CONFIG_PREFIX)).thenReturn(List.of(ext1));
		apiVendorExtensionStrategy2 = mock(ApiVendorExtensionStrategy.class);
		when(apiVendorExtensionStrategy2.getVendorExtensions(CONFIG_PREFIX)).thenReturn(List.of(ext2, ext3));
		defaultApiVendorExtensionService
				.setVendorExtensionStrategyList(List.of(apiVendorExtensionStrategy1, apiVendorExtensionStrategy2));
	}

	@Test
	public void getAllVendorExtensions()
	{
		//when
		final List<VendorExtension> vendorExtensions = defaultApiVendorExtensionService.getAllVendorExtensions(CONFIG_PREFIX);
		//then
		assertEquals(3, vendorExtensions.size());
		verify(apiVendorExtensionStrategy1).getVendorExtensions(CONFIG_PREFIX);
		verify(apiVendorExtensionStrategy2).getVendorExtensions(CONFIG_PREFIX);
	}
}
