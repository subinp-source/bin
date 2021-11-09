/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.provider.impl;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.product.PriceService;
import de.hybris.platform.searchservices.core.service.SnQualifier;
import de.hybris.platform.searchservices.core.service.SnSessionService;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import de.hybris.platform.searchservices.indexer.service.SnIndexerValueProvider;
import de.hybris.platform.searchservices.indexer.service.impl.AbstractSnIndexerValueProvider;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Implementation of {@link SnIndexerValueProvider} for product prices.
 */
public class ProductPriceSnIndexerValueProvider
		extends AbstractSnIndexerValueProvider<ProductModel, ProductPriceSnIndexerValueProvider.ProductPriceData>
{
	public static final String ID = "productPriceSnIndexerValueProvider";

	public static final String DEFAULT_CURRENCY_KEY = null;

	protected static final Set<Class<?>> SUPPORTED_QUALIFIER_CLASSES = Set.of(CurrencyModel.class);

	private CommonI18NService commonI18NService;
	private PriceService priceService;
	private SnSessionService snSessionService;

	@Override
	public Set<Class<?>> getSupportedQualifierClasses() throws SnIndexerException
	{
		return SUPPORTED_QUALIFIER_CLASSES;
	}

	@Override
	protected Object getFieldValue(final SnIndexerContext indexerContext, final SnIndexerFieldWrapper fieldWrapper,
			final ProductModel source, final ProductPriceData data)
	{
		if (MapUtils.isEmpty(data.getPrices()))
		{
			return null;
		}

		final Map<String, Double> prices = data.getPrices();

		if (fieldWrapper.isQualified())
		{
			final Map<String, Double> value = new HashMap<>();

			final List<SnQualifier> qualifiers = fieldWrapper.getQualifiers();
			for (final SnQualifier qualifier : qualifiers)
			{
				value.put(qualifier.getId(), prices.get(qualifier.getId()));
			}

			return value;
		}
		else
		{
			return prices.get(DEFAULT_CURRENCY_KEY);
		}
	}

	@Override
	protected ProductPriceData loadData(final SnIndexerContext indexerContext,
			final Collection<SnIndexerFieldWrapper> fieldWrappers, final ProductModel source) throws SnIndexerException
	{
		final Map<String, Double> prices = loadPrices(fieldWrappers, source);

		final ProductPriceData data = new ProductPriceData();
		data.setPrices(prices);

		return data;
	}

	protected Map<String, Double> loadPrices(final Collection<SnIndexerFieldWrapper> fieldWrappers, final ProductModel source)
	{
		final Map<String, Double> productPrices = new HashMap<>();

		for (final SnIndexerFieldWrapper fieldWrapper : fieldWrappers)
		{
			if (fieldWrapper.isQualified())
			{
				loadQualifiedPrices(fieldWrapper, source, productPrices);
			}
			else
			{
				loadDefaultPrice(source, productPrices);
			}
		}

		return productPrices;
	}

	protected void loadQualifiedPrices(final SnIndexerFieldWrapper fieldWrapper, final ProductModel source,
			final Map<String, Double> data)
	{
		try
		{
			snSessionService.initializeSession();

			final List<SnQualifier> qualifiers = fieldWrapper.getQualifiers();
			for (final SnQualifier qualifier : qualifiers)
			{
				data.computeIfAbsent(qualifier.getId(), key -> {
					final CurrencyModel currency = qualifier.getAs(CurrencyModel.class);

					commonI18NService.setCurrentCurrency(currency);

					final List<PriceInformation> priceInformations = priceService.getPriceInformationsForProduct(source);
					if (CollectionUtils.isEmpty(priceInformations))
					{
						return null;
					}

					return priceInformations.get(0).getPriceValue().getValue();
				});
			}
		}
		finally
		{
			snSessionService.destroySession();
		}
	}

	protected void loadDefaultPrice(final ProductModel source, final Map<String, Double> data)
	{
		data.computeIfAbsent(DEFAULT_CURRENCY_KEY, key -> {
			final List<PriceInformation> priceInformations = priceService.getPriceInformationsForProduct(source);
			if (CollectionUtils.isEmpty(priceInformations))
			{
				return null;
			}

			return priceInformations.get(0).getPriceValue().getValue();
		});
	}

	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	public PriceService getPriceService()
	{
		return priceService;
	}

	@Required
	public void setPriceService(final PriceService priceService)
	{
		this.priceService = priceService;
	}

	public SnSessionService getSnSessionService()
	{
		return snSessionService;
	}

	@Required
	public void setSnSessionService(final SnSessionService snSessionService)
	{
		this.snSessionService = snSessionService;
	}

	protected static class ProductPriceData
	{
		private Map<String, Double> prices;

		public Map<String, Double> getPrices()
		{
			return prices;
		}

		public void setPrices(final Map<String, Double> prices)
		{
			this.prices = prices;
		}
	}
}
