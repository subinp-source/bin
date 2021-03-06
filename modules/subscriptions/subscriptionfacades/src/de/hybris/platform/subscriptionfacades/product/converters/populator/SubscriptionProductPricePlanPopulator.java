/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionfacades.product.converters.populator;

import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.subscriptionservices.price.SubscriptionCommercePriceService;

import de.hybris.platform.subscriptionservices.subscription.SubscriptionProductService;
import org.springframework.beans.factory.annotation.Required;


/**
 * Populate the subscription product data with the subscription price plan information.
 *
 * @param <SOURCE>
 *           source class
 * @param <TARGET>
 *           target class
 */
public class SubscriptionProductPricePlanPopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends
		AbstractProductPopulator<SOURCE, TARGET>
{
	private Populator<SubscriptionPricePlanModel, SubscriptionPricePlanData> pricePlanOneTimeChargePopulator;
	private Populator<SubscriptionPricePlanModel, SubscriptionPricePlanData> pricePlanRecurringChargePopulator;
	private Converter<SubscriptionPricePlanModel, SubscriptionPricePlanData> pricePlanUsageChargeConverter;
	private SubscriptionCommercePriceService commercePriceService;
	private SubscriptionProductService subscriptionProductService;

	@Override
	public void populate(final SOURCE source, final TARGET target) throws ConversionException
	{
		if (getSubscriptionProductService().isSubscription(source))
		{
			final SubscriptionPricePlanModel pricePlanModel = getCommercePriceService().getSubscriptionPricePlanForProduct(source);

			if (pricePlanModel != null)
			{
				final SubscriptionPricePlanData pricePlanData = getPricePlanUsageChargeConverter().convert(pricePlanModel);
				pricePlanData.setName(pricePlanModel.getName());
				getPricePlanOneTimeChargePopulator().populate(pricePlanModel, pricePlanData);
				getPricePlanRecurringChargePopulator().populate(pricePlanModel, pricePlanData);
				if (target.getPrice() != null)
				{
					pricePlanData.setCurrencyIso(target.getPrice().getCurrencyIso());
					pricePlanData.setMaxQuantity(target.getPrice().getMaxQuantity());
					pricePlanData.setMinQuantity(target.getPrice().getMinQuantity());
				}
				target.setPrice(pricePlanData);
			}
		}
	}

	protected Populator<SubscriptionPricePlanModel, SubscriptionPricePlanData> getPricePlanOneTimeChargePopulator()
	{
		return pricePlanOneTimeChargePopulator;
	}

	@Required
	public void setPricePlanOneTimeChargePopulator(
			final Populator<SubscriptionPricePlanModel, SubscriptionPricePlanData> pricePlanOneTimeChargePopulator)
	{
		this.pricePlanOneTimeChargePopulator = pricePlanOneTimeChargePopulator;
	}

	protected Populator<SubscriptionPricePlanModel, SubscriptionPricePlanData> getPricePlanRecurringChargePopulator()
	{
		return pricePlanRecurringChargePopulator;
	}

	@Required
	public void setPricePlanRecurringChargePopulator(
			final Populator<SubscriptionPricePlanModel, SubscriptionPricePlanData> pricePlanRecurringChargePopulator)
	{
		this.pricePlanRecurringChargePopulator = pricePlanRecurringChargePopulator;
	}

	protected Converter<SubscriptionPricePlanModel, SubscriptionPricePlanData> getPricePlanUsageChargeConverter()
	{
		return pricePlanUsageChargeConverter;
	}

	@Required
	public void setPricePlanUsageChargeConverter(
			final Converter<SubscriptionPricePlanModel, SubscriptionPricePlanData> pricePlanUsageChargeConverter)
	{
		this.pricePlanUsageChargeConverter = pricePlanUsageChargeConverter;
	}

	protected SubscriptionCommercePriceService getCommercePriceService()
	{
		return commercePriceService;
	}

	@Required
	public void setCommercePriceService(final SubscriptionCommercePriceService commercePriceService)
	{
		this.commercePriceService = commercePriceService;
	}

	/**
	 * @return subscription product service.
	 */
	protected SubscriptionProductService getSubscriptionProductService()
	{
		return subscriptionProductService;
	}

	@Required
	public void setSubscriptionProductService(final SubscriptionProductService subscriptionProductService)
	{
		this.subscriptionProductService = subscriptionProductService;
	}

}
