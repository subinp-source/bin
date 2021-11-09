/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.i18n;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.zkoss.util.Locales;
import org.zkoss.web.Attributes;
import org.zkoss.zk.ui.Session;

import com.hybris.cockpitng.core.util.CockpitProperties;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class BackofficeLocaleServiceTest
{
	@Mock
	private I18NService i18nService;

	@Mock
	private Session session;

	@Mock
	private CockpitProperties globalProperties;

	@Spy
	@InjectMocks
	private BackofficeLocaleServiceUnderTest localeService;

	@Before
	public void setUp()
	{
		doReturn(Optional.of(session)).when(localeService).lookupZkSession();
	}

	@Test
	public void testChangeCurrentLocale()
	{
		final Locale localeEN = Locale.ENGLISH;
		localeService.setCurrentLocale(localeEN);
		verify(session, times(1)).setAttribute(Attributes.PREFERRED_LOCALE, localeEN);
		verify(i18nService, times(1)).setCurrentLocale(localeEN);
		assertEquals(Locales.getThreadLocal(), localeEN);
		assertEquals(localeService.getCurrentLocale(), localeEN);

		final Locale localeDE = Locale.GERMAN;
		localeService.setCurrentLocale(localeDE);
		verify(session, times(1)).setAttribute(Attributes.PREFERRED_LOCALE, localeDE);
		verify(i18nService, times(1)).setCurrentLocale(localeDE);
		assertEquals(Locales.getThreadLocal(), localeDE);
		assertEquals(localeService.getCurrentLocale(), localeDE);
	}

	@Test
	public void getAllUILocalesShouldReturnLocalesFromParsedList()
	{
		//given
		doReturn("en,pl,de").when(globalProperties).getProperty("lang.packs");
		final Locale polish = new Locale("pl");
		doReturn(new HashSet(Arrays.asList(Locale.ENGLISH, Locale.GERMAN, polish, Locale.JAPANESE, Locale.FRENCH)))
				.when(i18nService).getSupportedLocales();

		//when
		final List<Locale> uiLocales = localeService.getAllUILocales();

		//then
		assertThat(uiLocales).hasSize(3);
		assertThat(uiLocales).containsOnly(Locale.ENGLISH, Locale.GERMAN, polish);
	}

	@Test
	public void getAllUILocalesShouldReturnEmptyCollectionIfNoLanguagesAreSet()
	{
		//given
		doReturn(null).when(globalProperties).getProperty("lang.packs");

		//when
		final List<Locale> uiLocales = localeService.getAllUILocales();

		//then
		assertThat(uiLocales).isEmpty();
	}

	@Test
	public void getAllUILocalesShouldReturnEmptyCollectionIfNoLanguagesAreEffectivelySet()
	{
		//given
		doReturn(",,").when(globalProperties).getProperty("lang.packs");

		//when
		final List<Locale> uiLocales = localeService.getAllUILocales();

		//then
		assertThat(uiLocales).isEmpty();
	}

	public static class BackofficeLocaleServiceUnderTest extends BackofficeLocaleService
	{
		@Override
		protected Optional<Session> lookupZkSession()
		{
			throw new UnsupportedOperationException();
		}
	}
}
