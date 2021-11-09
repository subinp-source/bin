/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.cache.keyprovider.item;

import de.hybris.platform.cmsfacades.rendering.cache.keyprovider.RenderingItemCacheKeyProvider;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default cache key generator for {@link ItemModel}.
 */
public class DefaultItemRenderingCacheKeyProvider implements RenderingItemCacheKeyProvider<ItemModel>
{
	private CommerceCommonI18NService commerceCommonI18NService;

	@Override
	public String getKey(final ItemModel item)
	{
		final StringBuilder key = new StringBuilder();
		key.append(item.getPk().getLongValueAsString());
		key.append(item.getModifiedtime());
		final CurrencyModel currentCurrency = getCommerceCommonI18NService().getCurrentCurrency();
		final LanguageModel currentLanguage = getCommerceCommonI18NService().getCurrentLanguage();
		key.append(currentCurrency.getIsocode());
		key.append(currentLanguage.getIsocode());

		return key.toString();
	}

	public CommerceCommonI18NService getCommerceCommonI18NService()
	{
		return commerceCommonI18NService;
	}

	@Required
	public void setCommerceCommonI18NService(final CommerceCommonI18NService commerceCommonI18NService)
	{
		this.commerceCommonI18NService = commerceCommonI18NService;
	}
}
