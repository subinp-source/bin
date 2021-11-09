/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.odata2services.odata.persistence;

import de.hybris.platform.integrationservices.service.IntegrationLocalizationService;
import de.hybris.platform.integrationservices.service.impl.DefaultIntegrationLocalizationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Required;

/**
 * @deprecated use {@link DefaultIntegrationLocalizationService}
 *
 * Default implementation of the {@link ODataLocalizationService}
 */
@Deprecated(since = "1905.11-CEP", forRemoval = true)
public class DefaultODataLocalizationService implements ODataLocalizationService
{
	private CommonI18NService commonI18NService;
	private IntegrationLocalizationService localizationService;

	@Override
	public Locale getLocaleForLanguage(final String isoCode)
	{
		return getLocalizationService().getSupportedLocale(isoCode)
				.orElseThrow(() -> new LanguageNotSupportedException(isoCode));
	}

	@Override
	public Locale[] getSupportedLocales()
	{
		return getLocalizationService().getAllSupportedLocales().toArray(Locale[]::new);
	}

	@Override
	public Locale getCommerceSuiteLocale()
	{
		return getLocalizationService().getDefaultLocale();
	}

	protected IntegrationLocalizationService getLocalizationService()
	{
		return localizationService;
	}

	public void setLocalizationService(final IntegrationLocalizationService service)
	{
		localizationService = service;
	}

	/**
	 * Retrieves injected {@code CommonI18NService} implementation
	 * @return the injected service
	 * @deprecated use {@link #getLocalizationService()}
	 */
	@Deprecated(since = "1905.08-CEP", forRemoval = true)
	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	/**
	 * Injects {@code CommonI18NService} to use.
	 * @param commonI18NService a service implementation to inject
	 * @deprecated use {@link #setLocalizationService(IntegrationLocalizationService)}
	 */
	@Required
	@Deprecated(since = "1905.08-CEP", forRemoval = true)
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}
}
