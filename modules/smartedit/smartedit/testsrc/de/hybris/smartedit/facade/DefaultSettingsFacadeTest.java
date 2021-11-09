/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.smartedit.facade;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.smartedit.facade.impl.DefaultSettingsFacade;

import java.util.Arrays;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultSettingsFacadeTest
{
	@Spy
	private DefaultSettingsFacade defaultSettingsFacade;

	@Test
	public void testDefaultSettingsFacade()
	{
		Mockito.doReturn("true").when(defaultSettingsFacade).getSetting("smartedit.sso.enabled");
		Mockito.doReturn("false").when(defaultSettingsFacade).getSetting("cms.components.allowUnsafeJavaScript");
		Mockito.doReturn("someString").when(defaultSettingsFacade).getSetting("smartedit.globalBasePath");
		Mockito.doReturn(" a , b,,, ,c, d, e,f,g ").when(defaultSettingsFacade).getSetting("smartedit.validImageMimeTypeCodes");

		final Map<String, Object> settings = defaultSettingsFacade.getSettings();

		assertEquals(settings.get("smartedit.sso.enabled"), "true");
		assertEquals(settings.get("cms.components.allowUnsafeJavaScript"), "false");
		assertEquals(settings.get("smartedit.globalBasePath"), "someString");
		assertThat(settings.get("smartedit.validImageMimeTypeCodes"), is(Arrays.asList("a", "b", "c", "d", "e", "f", "g")));
	}
}