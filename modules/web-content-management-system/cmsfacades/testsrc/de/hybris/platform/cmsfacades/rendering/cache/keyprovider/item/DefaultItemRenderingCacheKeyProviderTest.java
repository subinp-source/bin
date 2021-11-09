/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.cache.keyprovider.item;

import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultItemRenderingCacheKeyProviderTest
{
	private static final String CURRENCY_ISO_CODE = "CurrencyIsoCode";
	private static final String LANGUAGE_ISO_CODE = "LanguageIsoCode";

	@InjectMocks
	private DefaultItemRenderingCacheKeyProvider defaultItemRenderingCacheKeyProvider;

	@Mock
	private CommerceCommonI18NService commerceCommonI18NService;

	@Mock
	private CurrencyModel currentCurrency;

	@Mock
	private LanguageModel languageModel;

	@Mock
	private ItemModel item;

	private final PK primaryKey = PK.parse("123");
	private final Date modifiedType = new Date();

	@Before
	public void setup()
	{
		when(item.getPk()).thenReturn(primaryKey);
		when(item.getModifiedtime()).thenReturn(modifiedType);
		when(currentCurrency.getIsocode()).thenReturn(CURRENCY_ISO_CODE);
		when(languageModel.getIsocode()).thenReturn(LANGUAGE_ISO_CODE);

		when(commerceCommonI18NService.getCurrentCurrency()).thenReturn(currentCurrency);
		when(commerceCommonI18NService.getCurrentLanguage()).thenReturn(languageModel);
	}

	@Test
	public void shouldReturnCacheKeyForItem()
	{
		// WHEN
		final String key = defaultItemRenderingCacheKeyProvider.getKey(item);

		// THEN
		final String resultKey = primaryKey.getLongValueAsString() + modifiedType.toString() + CURRENCY_ISO_CODE + LANGUAGE_ISO_CODE;
		Assert.assertEquals(key, resultKey);
	}
}
