/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.services.impl;

import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.PriceModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.PriceSummaryModel;
import de.hybris.platform.sap.productconfig.services.intf.PricingService;
import de.hybris.platform.sap.productconfig.services.intf.ProductConfigurationPricingStrategy;
import de.hybris.platform.sap.productconfig.services.intf.ProductConfigurationService;
import de.hybris.platform.sap.productconfig.services.strategies.lifecycle.intf.ConfigurationAbstractOrderEntryLinkStrategy;
import de.hybris.platform.sap.productconfig.services.strategies.lifecycle.intf.ConfigurationAbstractOrderIntegrationStrategy;
import de.hybris.platform.sap.productconfig.services.strategies.lifecycle.intf.ConfigurationCopyStrategy;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.PriceValue;

import java.math.BigDecimal;

import org.apache.log4j.Logger;


/**
 * Default implementation for pricing for configurable products
 */
public class ProductConfigurationPricingStrategyImpl implements ProductConfigurationPricingStrategy
{

	private ProductConfigurationService configurationService;
	private PricingService pricingService;
	private CommerceCartService commerceCartService;
	private ModelService modelService;
	private ConfigurationAbstractOrderEntryLinkStrategy configurationAbstractOrderEntryLinkStrategy;
	private ConfigurationCopyStrategy configCopyStrategy;
	private CommonI18NService i18NService;
	private ConfigurationAbstractOrderIntegrationStrategy configurationAbstractOrderIntegrationStrategy;

	private static final Logger LOG = Logger.getLogger(ProductConfigurationPricingStrategyImpl.class);

	/**
	 * @deprecated since 20.05
	 */
	@Override
	@Deprecated(since = "2005", forRemoval = true)
	public boolean updateCartEntryBasePrice(final AbstractOrderEntryModel entry)
	{
		final PriceModel currentTotalPrice = calculateBasePriceModelForConfiguration(entry);
		boolean cartEntryUpdated = false;
		final Double newPrice = hasBasePriceChanged(entry, currentTotalPrice);
		if (newPrice != null)
		{
			entry.setBasePrice(newPrice);
			LOG.debug("Base price: " + entry.getBasePrice() + " is set for the cart entry with pk: " + entry.getPk());
			cartEntryUpdated = true;
		}
		return cartEntryUpdated;
	}

	@Override
	public PriceValue calculateBasePriceForConfiguration(final AbstractOrderEntryModel entry)
	{
		final PriceModel basePriceModel = calculateBasePriceModelForConfiguration(entry);
		if (basePriceModel == null || PriceModel.NO_PRICE.equals(basePriceModel) || PriceModel.PRICE_NA.equals(basePriceModel))
		{
			LOG.debug("Price obtained for configuration attached to entry " + entry.getPk().toString() + " with product "
					+ entry.getProduct().getCode() + " is null or NO_PRICE or PRICE_NA");
			return createZeroPrice(entry);
		}
		LOG.debug("Price obtained for configuration attached to entry " + entry.getPk().toString() + " with product "
				+ entry.getProduct().getCode() + " : " + basePriceModel.getPriceValue().doubleValue() + " "
				+ basePriceModel.getCurrency());
		return new PriceValue(basePriceModel.getCurrency(), basePriceModel.getPriceValue().doubleValue(),
				entry.getOrder().getNet().booleanValue());
	}

	protected PriceValue createZeroPrice(final AbstractOrderEntryModel entry)
	{
		return new PriceValue(getI18NService().getCurrentCurrency().getIsocode(), Double.valueOf(0),
				entry.getOrder().getNet().booleanValue());
	}

	protected PriceModel calculateBasePriceModelForConfiguration(final AbstractOrderEntryModel entry)
	{
		final String configId = getConfigIdForCartEntry(entry);
		return retrieveCurrentTotalPrice(configId, entry);
	}

	protected String getConfigIdForCartEntry(final AbstractOrderEntryModel entry)
	{
		final String pk = entry.getPk().toString();
		String configId = getAbstractOrderEntryLinkStrategy().getConfigIdForCartEntry(pk);
		if (null == configId)
		{
			configId = getAbstractOrderEntryLinkStrategy().getDraftConfigIdForCartEntry(pk);
		}
		LOG.debug("Found config id " + configId + " for cart entry " + pk);
		return configId;
	}

	protected Double hasBasePriceChanged(final AbstractOrderEntryModel entry, final PriceModel newPrice)
	{
		if (newPrice != null && newPrice.hasValidPrice())
		{
			final BigDecimal updatedPrice = newPrice.getPriceValue();
			final BigDecimal oldPrice = BigDecimal.valueOf(entry.getBasePrice());
			return updatedPrice.compareTo(oldPrice) != 0 ? updatedPrice.doubleValue() : null;
		}
		return null;
	}

	protected boolean hasBasePriceChanged(final AbstractOrderEntryModel entry)
	{
		final PriceModel currentTotalPrice = calculateBasePriceModelForConfiguration(entry);

		return hasBasePriceChanged(entry, currentTotalPrice) != null;

	}

	@Override
	public boolean updateCartEntryPrices(final AbstractOrderEntryModel entry, final boolean calculateCart,
			final CommerceCartParameter passedParameter)
	{
		if (hasBasePriceChanged(entry))
		{
			entry.setCalculated(false);
			entry.getOrder().setCalculated(false);
			getModelService().save(entry);
			getModelService().save(entry.getOrder());
			if (calculateCart)
			{
				if (passedParameter == null)
				{
					final CommerceCartParameter parameter = getParametersForCartUpdate(entry);
					getCommerceCartService().calculateCart(parameter);
				}
				else
				{
					getCommerceCartService().calculateCart(passedParameter);
				}
			}
			return true;
		}
		return false;
	}

	protected CommerceCartParameter getParametersForCartUpdate(final AbstractOrderEntryModel entry)
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		parameter.setCart((CartModel) entry.getOrder());
		final String configId = getAbstractOrderEntryLinkStrategy().getConfigIdForCartEntry(entry.getPk().toString());
		parameter.setConfigId(configId);
		return parameter;
	}

	protected PriceModel retrieveCurrentTotalPrice(final String configId, final AbstractOrderEntryModel entry)
	{
		if (getPricingService().isActive())
		{
			LOG.debug("Asynchronous pricing is active.");
			if (configId == null)
			{
				return null;
			}
			final PriceSummaryModel priceSummary = getPricingService().getPriceSummary(configId);
			if (priceSummary == null)
			{
				return null;
			}
			validateCurrency(priceSummary.getCurrentTotalPrice());
			return priceSummary.getCurrentTotalPrice();
		}
		else
		{
			LOG.debug("Asynchronous pricing is NOT active.");
			if (configId == null)
			{
				final ConfigModel configModel = getConfigurationAbstractOrderIntegrationStrategy()
						.getConfigurationForAbstractOrderEntry(entry);
				return configModel.getCurrentTotalPrice();
			}
			PriceModel currentTotalPrice = getConfigurationService().retrieveConfigurationModel(configId).getCurrentTotalPrice();
			if (currentTotalPrice == null)
			{
				return null;
			}
			if (!PriceModel.NO_PRICE.equals(currentTotalPrice) && !PriceModel.PRICE_NA.equals(currentTotalPrice)
					&& !isSessionCurrencyMatching(currentTotalPrice.getCurrency()))
			{
				LOG.debug("Session currency has changed, so configuration model has to be reloaded with new currency");
				currentTotalPrice = reloadCurrentTotalWithCurrentCurrency(entry);
			}
			validateCurrency(currentTotalPrice);
			return currentTotalPrice;
		}
	}

	protected void validateCurrency(final PriceModel price)
	{
		if (price == null || PriceModel.NO_PRICE.equals(price) || PriceModel.PRICE_NA.equals(price))
		{
			return;
		}
		if (!isSessionCurrencyMatching(price.getCurrency()))
		{
			LOG.debug("Session currency is " + getI18NService().getCurrentCurrency().getIsocode() + ". Price only found for "
					+ price.getCurrency());
			throw new IllegalStateException("Cannot retrieve price for session currency. Turn on debug log level for details.");
		}
	}

	protected void recreateConfigurationWithCurrentCurrency(final String configId, final AbstractOrderEntryModel entry,
			final boolean draft)
	{
		if (configId != null)
		{
			final String externalConfiguration = getConfigurationService().retrieveExternalConfiguration(configId);
			final String newConfigId = getConfigCopyStrategy().deepCopyConfiguration(configId, entry.getProduct().getCode(),
					externalConfiguration, true);
			getConfigurationService().releaseSession(configId);
			if (draft)
			{
				getAbstractOrderEntryLinkStrategy().setDraftConfigIdForCartEntry(entry.getPk().toString(), newConfigId);
			}
			else
			{
				getAbstractOrderEntryLinkStrategy().setConfigIdForCartEntry(entry.getPk().toString(), newConfigId);
			}
			LOG.debug("Recreated configuration " + configId + " with new configuration " + newConfigId + " using session currency.");
		}
	}

	protected PriceModel reloadCurrentTotalWithCurrentCurrency(final AbstractOrderEntryModel entry)
	{
		final String configIdForCartEntry = getAbstractOrderEntryLinkStrategy().getConfigIdForCartEntry(entry.getPk().toString());
		recreateConfigurationWithCurrentCurrency(configIdForCartEntry, entry, false);
		final String draftConfigIdForCartEntry = getAbstractOrderEntryLinkStrategy()
				.getDraftConfigIdForCartEntry(entry.getPk().toString());
		recreateConfigurationWithCurrentCurrency(draftConfigIdForCartEntry, entry, true);

		return getConfigurationService().retrieveConfigurationModel(getConfigIdForCartEntry(entry)).getCurrentTotalPrice();
	}

	protected boolean isSessionCurrencyMatching(final String priceCurrencyIso)
	{
		return priceCurrencyIso.equals(getI18NService().getCurrentCurrency().getIsocode());
	}

	@Override
	public boolean isCartPricingErrorPresent(final ConfigModel configModel)
	{
		return configModel.hasPricingError();
	}

	protected ProductConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	protected PricingService getPricingService()
	{
		return pricingService;
	}

	protected CommerceCartService getCommerceCartService()
	{
		return commerceCartService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected ConfigurationAbstractOrderEntryLinkStrategy getAbstractOrderEntryLinkStrategy()
	{
		return configurationAbstractOrderEntryLinkStrategy;
	}

	protected CommonI18NService getI18NService()
	{
		return i18NService;
	}

	protected ConfigurationCopyStrategy getConfigCopyStrategy()
	{
		return configCopyStrategy;
	}

	public void setConfigurationService(final ProductConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	public void setPricingService(final PricingService pricingService)
	{
		this.pricingService = pricingService;
	}

	public void setCommerceCartService(final CommerceCartService commerceCartService)
	{
		this.commerceCartService = commerceCartService;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public void setConfigurationAbstractOrderEntryLinkStrategy(
			final ConfigurationAbstractOrderEntryLinkStrategy configurationAbstractOrderEntryLinkStrategy)
	{
		this.configurationAbstractOrderEntryLinkStrategy = configurationAbstractOrderEntryLinkStrategy;
	}

	public void setConfigCopyStrategy(final ConfigurationCopyStrategy configCopyStrategy)
	{
		this.configCopyStrategy = configCopyStrategy;
	}

	public void setI18NService(final CommonI18NService i18nService)
	{
		i18NService = i18nService;
	}

	protected ConfigurationAbstractOrderIntegrationStrategy getConfigurationAbstractOrderIntegrationStrategy()
	{
		return configurationAbstractOrderIntegrationStrategy;
	}

	public void setConfigurationAbstractOrderIntegrationStrategy(
			final ConfigurationAbstractOrderIntegrationStrategy configurationAbstractOrderIntegrationStrategy)
	{
		this.configurationAbstractOrderIntegrationStrategy = configurationAbstractOrderIntegrationStrategy;
	}
}
