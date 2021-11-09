/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.addonsupport;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.bootstrap.config.ExtensionInfo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;


/**
 * test for {@link RequiredAddOnsNameProvider}
 */
@UnitTest
public class RequiredAddOnsNameProviderTest
{
	@Spy
	private final RequiredAddOnsNameProvider provider = new RequiredAddOnsNameProvider();

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetAddOnsWithEmptyExtName()
	{
		final List<String> list = provider.getAddOns(null);
		Assert.assertEquals(0, list.size());
	}

	@Test
	public void testGetAddOns()
	{
		final ExtensionInfo extensionInfo = Mockito.mock(ExtensionInfo.class);

		final ExtensionInfo addon1 = Mockito.mock(ExtensionInfo.class);
		Mockito.when(addon1.getName()).thenReturn("xyz");

		final ExtensionInfo addon2 = Mockito.mock(ExtensionInfo.class);
		Mockito.when(addon2.getName()).thenReturn("abc");

		Mockito.when(extensionInfo.getAllRequiredExtensionInfos())
				.thenReturn(Stream.of(addon1, addon2).collect(Collectors.toCollection(HashSet::new)));

		Mockito.doReturn(extensionInfo).when(provider).getExtensionInfo(Mockito.any());
		Mockito.doReturn(true).when(provider).isAddOnExtension(Mockito.any());
		Mockito.doReturn(Arrays.asList("xyz", "abc")).when(provider).getExtensionNames();


		final List<String> list = provider.getAddOns("yacceleratorstorefront");
		Assert.assertEquals(Arrays.asList("abc", "xyz"), list);
	}
}
