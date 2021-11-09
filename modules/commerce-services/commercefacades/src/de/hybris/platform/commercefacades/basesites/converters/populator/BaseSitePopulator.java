/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.basesites.converters.populator;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.basesite.data.BaseSiteData;
import de.hybris.platform.commercefacades.basestore.data.BaseStoreData;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.store.BaseStoreModel;

import org.springframework.beans.factory.annotation.Required;

/**
 * Populates {@link BaseSiteData} from {@link BaseSiteModel}
 */
public class BaseSitePopulator implements Populator<BaseSiteModel, BaseSiteData>
{
	private Converter<LanguageModel, LanguageData> languageConverter;
	private Converter<BaseStoreModel, BaseStoreData> baseStoreConverter;

	@Override
	public void populate(final BaseSiteModel source, final BaseSiteData target) throws ConversionException
	{
		target.setUid(source.getUid());
		target.setName(source.getName());
		target.setStores(getBaseStoreConverter().convertAll(source.getStores()));
		target.setLocale(source.getLocale());

		if (source.getChannel() != null)
		{
			target.setChannel(source.getChannel().getCode());
		}
		if (source.getTheme() != null)
		{
			target.setTheme(source.getTheme().getCode());
		}
		if (source.getDefaultLanguage() != null)
		{
			target.setDefaultLanguage(getLanguageConverter().convert(source.getDefaultLanguage()));
		}
	}

	protected Converter<LanguageModel, LanguageData> getLanguageConverter()
	{
		return languageConverter;
	}

	@Required
	public void setLanguageConverter(final Converter<LanguageModel, LanguageData> languageConverter)
	{
		this.languageConverter = languageConverter;
	}

	protected Converter<BaseStoreModel, BaseStoreData> getBaseStoreConverter()
	{
		return baseStoreConverter;
	}

	@Required
	public void setBaseStoreConverter(final Converter<BaseStoreModel, BaseStoreData> baseStoreConverter)
	{
		this.baseStoreConverter = baseStoreConverter;
	}
}
