/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.smarteditwebservices.i18n.facade.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.smarteditwebservices.data.SmarteditLanguageData;

import java.util.Locale;



/**
 * Populator to convert {@link Locale} into {@link SmarteditLanguageData}
 */
public class SmarteditLanguagePopulator implements Populator<Locale, SmarteditLanguageData>
{

	@Override
	public void populate(final Locale source, final SmarteditLanguageData target) throws ConversionException
	{
		target.setName(source.getDisplayName(source));
		target.setIsoCode(source.toLanguageTag().replace('-', '_'));
	}
}
