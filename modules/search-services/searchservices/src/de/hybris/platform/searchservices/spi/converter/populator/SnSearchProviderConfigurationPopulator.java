/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.spi.converter.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.searchservices.model.AbstractSnSearchProviderConfigurationModel;
import de.hybris.platform.searchservices.spi.data.AbstractSnSearchProviderConfiguration;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Populates {@link AbstractSnSearchProviderConfiguration} from {@link AbstractSnSearchProviderConfigurationModel}.
 */
public class SnSearchProviderConfigurationPopulator
		implements Populator<AbstractSnSearchProviderConfigurationModel, AbstractSnSearchProviderConfiguration>
{
	private I18NService i18NService;

	@Override
	public void populate(final AbstractSnSearchProviderConfigurationModel source,
			final AbstractSnSearchProviderConfiguration target)
	{
		target.setId(source.getId());
		target.setName(buildLocalizedName(source));

		final List<String> listeners = source.getListeners();
		if (CollectionUtils.isNotEmpty(listeners))
		{
			target.setListeners(new ArrayList<>(listeners));
		}
	}

	protected Map<Locale, String> buildLocalizedName(final AbstractSnSearchProviderConfigurationModel source)
	{
		final Set<Locale> supportedLocales = i18NService.getSupportedLocales();
		final Map<Locale, String> target = new LinkedHashMap<>();

		for (final Locale locale : supportedLocales)
		{
			target.put(locale, source.getName(locale));
		}

		return target;
	}

	public I18NService getI18NService()
	{
		return i18NService;
	}

	@Required
	public void setI18NService(final I18NService i18nService)
	{
		i18NService = i18nService;
	}
}
