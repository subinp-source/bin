/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.converter.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.searchservices.admin.data.SnCurrency;
import de.hybris.platform.searchservices.admin.data.SnField;
import de.hybris.platform.searchservices.model.SnFieldModel;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populates {@link SnField} from {@link SnFieldModel}.
 */
public class SnCurrencyPopulator implements Populator<CurrencyModel, SnCurrency>
{
	private I18NService i18NService;

	@Override
	public void populate(final CurrencyModel source, final SnCurrency target)
	{
		target.setId(source.getIsocode());
		target.setName(buildLocalizedName(source));
	}

	protected Map<Locale, String> buildLocalizedName(final CurrencyModel source)
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
