/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.basesites.converters.populator;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.basesite.data.BaseSiteData;
import de.hybris.platform.commercefacades.basestore.data.BaseStoreData;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.commerceservices.enums.SiteTheme;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class BaseSitePopulatorTest
{
	public static final String BASE_SITE_UID = "baseSiteUid";
	public static final String BASE_SITE_NAME = "baseSiteName";
	public static final String BASE_SITE_THEME = "baseSiteTheme";
	public static final String BASE_SITE_CHANNEL = "B2C";
	public static final String BASE_SITE_LOCALE = "baseSiteLocale";

	@InjectMocks
	private final BaseSitePopulator baseSitePopulator = new BaseSitePopulator();

	private BaseSiteData baseSiteData;

	@Mock
	private SiteTheme siteTheme;
	@Mock
	private Converter<LanguageModel, LanguageData> languageConverter;
	@Mock
	private Converter<BaseStoreModel, BaseStoreData> baseStoreConverter;
	@Mock
	private LanguageModel languageModel;
	@Mock
	private LanguageData languageData;
	@Mock
	private BaseStoreModel baseStoreModel;
	@Mock
	private BaseStoreData baseStoreData;
	@Mock
	private BaseSiteModel baseSiteModel;

	@Before
	public void setUp() throws Exception
	{
		baseSiteData = new BaseSiteData();
		when(baseSiteModel.getUid()).thenReturn(BASE_SITE_UID);
		when(baseSiteModel.getName()).thenReturn(BASE_SITE_NAME);
		when(baseSiteModel.getStores()).thenReturn(Collections.singletonList(baseStoreModel));
		when(siteTheme.getCode()).thenReturn(BASE_SITE_THEME);
		when(baseSiteModel.getTheme()).thenReturn(siteTheme);
		when(baseSiteModel.getDefaultLanguage()).thenReturn(languageModel);
		when(baseSiteModel.getChannel()).thenReturn(SiteChannel.B2C);
		when(baseSiteModel.getLocale()).thenReturn(BASE_SITE_LOCALE);

		when(baseStoreConverter.convertAll(Collections.singletonList(baseStoreModel)))
				.thenReturn(Collections.singletonList(baseStoreData));
		when(languageConverter.convert(languageModel)).thenReturn(languageData);
	}

	@Test
	public void populate()
	{
		baseSitePopulator.populate(baseSiteModel, baseSiteData);
		assertEquals(BASE_SITE_UID, baseSiteData.getUid());
		assertEquals(BASE_SITE_NAME, baseSiteData.getName());
		assertEquals(1, baseSiteData.getStores().size());
		assertEquals(baseStoreData, baseSiteData.getStores().get(0));
		assertEquals(BASE_SITE_THEME, baseSiteData.getTheme());
		assertEquals(languageData, baseSiteData.getDefaultLanguage());
		assertEquals(BASE_SITE_CHANNEL, baseSiteData.getChannel());
		assertEquals(BASE_SITE_LOCALE, baseSiteData.getLocale());
	}

	@Test
	public void populateWithOptionalFieldsEqualToNull()
	{
		when(baseSiteModel.getChannel()).thenReturn(null);
		when(baseSiteModel.getTheme()).thenReturn(null);
		when(baseSiteModel.getDefaultLanguage()).thenReturn(null);
		when(baseSiteModel.getStores()).thenReturn(null);
		when(baseSiteModel.getLocale()).thenReturn(null);

		baseSitePopulator.populate(baseSiteModel, baseSiteData);

		assertEquals(BASE_SITE_UID, baseSiteData.getUid());
		assertEquals(BASE_SITE_NAME, baseSiteData.getName());

		assertNull(baseSiteData.getLocale());
		assertEquals(List.of(), baseSiteData.getStores());
		assertNull(baseSiteData.getChannel());
		assertNull(baseSiteData.getTheme());
		assertNull(baseSiteData.getDefaultLanguage());
	}
}
