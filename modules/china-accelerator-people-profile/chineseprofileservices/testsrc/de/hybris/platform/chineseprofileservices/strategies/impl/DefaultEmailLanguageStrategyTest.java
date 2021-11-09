/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
 package de.hybris.platform.chineseprofileservices.strategies.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultEmailLanguageStrategyTest
{
	@Mock
	private ConfigurationService configService;

	@Mock
	private Configuration configuration;

	@Mock
	private CommerceCommonI18NService commerceCommonI18NService;

	@Mock
	private CommonI18NService commonI18NService;

	@Mock
	private LanguageModel currentLanguageModel;

	private DefaultEmailLanguageStrategy emailLanguageStrategy;

	private static final String DEFAULT_EMAIL_LANGUAGE = "email.language.default";

	private static final String EN_ISOCODE = "en";

	private static final String INVALID_ISOCODE = "xxx";

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);

		final DefaultEmailLanguageStrategy emailLanguageStrategyObj = new DefaultEmailLanguageStrategy(configService,
				commerceCommonI18NService, commonI18NService);
		emailLanguageStrategy = Mockito.spy(emailLanguageStrategyObj);
		Mockito.when(configService.getConfiguration()).thenReturn(configuration);
		Mockito.when(commonI18NService.getCurrentLanguage()).thenReturn(currentLanguageModel);
	}

	@Test
	public void testGetDefaultEmailLanguage()
	{
		Mockito.when(configuration.getString(DEFAULT_EMAIL_LANGUAGE)).thenReturn(EN_ISOCODE);
		doReturn(true).when(emailLanguageStrategy).isValidEmailLanguage(EN_ISOCODE);
		final String emailLanguage = emailLanguageStrategy.getDefaultEmailLanguage();

		verify(emailLanguageStrategy).isValidEmailLanguage(EN_ISOCODE);
		assertEquals(EN_ISOCODE, emailLanguage);

	}

	@Test
	public void testGetDefaultEmailLanguageWithoutConfiguration()
	{
		Mockito.when(configuration.getString(DEFAULT_EMAIL_LANGUAGE)).thenReturn(StringUtils.EMPTY);
		Mockito.when(currentLanguageModel.getIsocode()).thenReturn(EN_ISOCODE);

		final String emailLanguage = emailLanguageStrategy.getDefaultEmailLanguage();
		assertEquals(EN_ISOCODE, emailLanguage);
	}

	@Test
	public void testGetDefaultEmailLanguageWithInvalidIsoCode()
	{
		Mockito.when(configuration.getString(DEFAULT_EMAIL_LANGUAGE)).thenReturn(INVALID_ISOCODE);
		Mockito.when(currentLanguageModel.getIsocode()).thenReturn(EN_ISOCODE);

		final String emailLanguage = emailLanguageStrategy.getDefaultEmailLanguage();
		verify(commonI18NService.getCurrentLanguage()).getIsocode();
		assertEquals(EN_ISOCODE, emailLanguage);
	}

}
