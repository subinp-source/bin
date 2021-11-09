/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.storesession.converters.populator;

import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.LanguageModel;

import java.util.Locale;

import org.apache.commons.lang.LocaleUtils;
import org.springframework.util.Assert;

public class LanguagePopulator<SOURCE extends LanguageModel, TARGET extends LanguageData> implements Populator<SOURCE, TARGET>
{

	@Override
	public void populate(final SOURCE source, final TARGET target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setIsocode(source.getIsocode());
		target.setName(source.getName());
		target.setActive(source.getActive().booleanValue());
		target.setNativeName(source.getName(toLocale(source)));
	}

	protected Locale toLocale(final LanguageModel source)
	{
		return LocaleUtils.toLocale(source.getIsocode());
	}
}
