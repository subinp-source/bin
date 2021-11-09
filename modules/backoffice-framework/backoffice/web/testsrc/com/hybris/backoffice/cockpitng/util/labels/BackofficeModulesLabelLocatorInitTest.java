/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.util.labels;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.zkoss.util.Locales;
import org.zkoss.util.resource.Labels;


@RunWith(MockitoJUnitRunner.class)
public class BackofficeModulesLabelLocatorInitTest
{

	public static final String SAMPLE_LOCATION = "/test/locator";
	public static final String EXT_NAME_DEMO_BACKOFFICE = "demobackoffice";
	public static final String EXT_NAME_PLACEHOLDER = "{extName}";
	@Spy
	private BackofficeModulesLabelLocatorInit locator;

	@Before
	public void setUp()
	{
		doReturn(Arrays.asList(EXT_NAME_DEMO_BACKOFFICE)).when(locator).getAllBackofficeExtensionNames();
		locator.setLocation(SAMPLE_LOCATION);
		locator.setName(EXT_NAME_PLACEHOLDER);
		locator.init();
	}

	@Test
	public void labelsGetLabelShouldFindLabelsAfterInitForSelectedLocale()
	{
		//given
		Locales.setThreadLocal(Locale.ENGLISH);

		//when
		final String check = Labels.getLabel("test.check");

		//then
		assertThat(check).isEqualTo("passed_en");
	}

	@Test
	public void labelsGetLabelShouldFindLabelsAfterInitForUndefinedLocale()
	{
		//given
		Locales.setThreadLocal(Locale.GERMAN);

		//when
		final String check = Labels.getLabel("test.check");

		//then
		assertThat(check).isEqualTo("passed");
	}

	@Test
	public void getNormalizedLocationShouldReturnEmptyOnBlankLocation()
	{
		assertThat(locator.getNormalizedLocation(null)).isEmpty();
		assertThat(locator.getNormalizedLocation(StringUtils.EMPTY)).isEmpty();
		assertThat(locator.getNormalizedLocation("     \t\n   ")).isEmpty();
	}

	@Test
	public void getNormalizedLocationShouldReturnUnchangedWithoutLeadingOrTrailingSlashes()
	{
		assertThat(locator.getNormalizedLocation("abc")).isEqualTo("abc");
	}

	@Test
	public void getNormalizedLocationShouldRemoveLeadingAndTrailingSlashes()
	{
		assertThat(locator.getNormalizedLocation("//")).isEmpty();
		assertThat(locator.getNormalizedLocation("//a")).isEqualTo("a");
		assertThat(locator.getNormalizedLocation("a//")).isEqualTo("a");
		assertThat(locator.getNormalizedLocation("/a")).isEqualTo("a");
		assertThat(locator.getNormalizedLocation("/b/")).isEqualTo("b");
	}

	@Test
	public void lookupCandidatePropertyFilesShouldFindTestBundles()
	{
		//when
		final Set<String> files = locator.lookupCandidatePropertyFiles(SAMPLE_LOCATION);

		//then
		assertThat(files).hasSize(2);
		assertThat(files).containsOnly("/test/locator/demobackoffice.properties", "/test/locator/demobackoffice_en.properties");
	}


}
