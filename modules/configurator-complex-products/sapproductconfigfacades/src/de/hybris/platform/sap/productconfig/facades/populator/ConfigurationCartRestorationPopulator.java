/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.facades.populator;

import de.hybris.platform.commercefacades.order.data.CartRestorationData;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartRestoration;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.order.model.AbstractOrderEntryProductInfoModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.services.impl.CPQConfigurableChecker;
import de.hybris.platform.sap.productconfig.services.intf.ProductConfigurationOrderIntegrationService;
import de.hybris.platform.sap.productconfig.services.strategies.impl.ProductConfigurationCartRestorationStrategyImpl;
import de.hybris.platform.sap.productconfig.services.strategies.lifecycle.intf.ConfigurationAbstractOrderIntegrationStrategy;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.List;


/**
 * Takes care of refreshing the inline configuration
 */
public class ConfigurationCartRestorationPopulator implements Populator<CommerceCartRestoration, CartRestorationData>
{
	private final ConfigurationAbstractOrderIntegrationStrategy configurationAbstractOrderIntegrationStrategy;
	private final CPQConfigurableChecker cpqConfigurableChecker;
	private final ConfigurationOrderEntryProductInfoModelPopulator configInfoPopulator;
	private final ProductConfigurationOrderIntegrationService configurationPricingOrderIntegrationService;
	private final ModelService modelService;


	public ConfigurationCartRestorationPopulator(
			final ConfigurationAbstractOrderIntegrationStrategy configurationAbstractOrderIntegrationStrategy,
			final CPQConfigurableChecker cpqConfigurableChecker,
			final ConfigurationOrderEntryProductInfoModelPopulator configInfoPopulator,
			final ProductConfigurationOrderIntegrationService configurationPricingOrderIntegrationService,
			final ModelService modelService)
	{
		this.configurationAbstractOrderIntegrationStrategy = configurationAbstractOrderIntegrationStrategy;
		this.cpqConfigurableChecker = cpqConfigurableChecker;
		this.configInfoPopulator = configInfoPopulator;
		this.configurationPricingOrderIntegrationService = configurationPricingOrderIntegrationService;
		this.modelService = modelService;
	}

	@Override
	public void populate(final CommerceCartRestoration source, final CartRestorationData target)
	{
		if (source != null)
		{
			for (final CommerceCartModification modification : source.getModifications())
			{
				final AbstractOrderEntryModel entry = modification.getEntry();
				if (isUpdateRequired(modification.getStatusCode(), entry.getProduct()))
				{

					final ConfigModel configModel = getConfigurationAbstractOrderIntegrationStrategy()
							.getConfigurationForAbstractOrderEntry(entry);
					addConfigAttributesToCartEntry(configModel, entry);
				}
			}
		}
	}

	protected boolean isUpdateRequired(final String status, final ProductModel product)
	{
		return ProductConfigurationCartRestorationStrategyImpl.REFRESH_INLINE_CONFIGURATION.equals(status)
				&& isConfigurableProduct(product);
	}

	protected void addConfigAttributesToCartEntry(final ConfigModel configModel, final AbstractOrderEntryModel entry)
	{
		final List<AbstractOrderEntryProductInfoModel> configInlineModels = new ArrayList<>();
		getConfigInfoPopulator().populate(configModel, configInlineModels);

		entry.setProductInfos(configInlineModels);
		for (final AbstractOrderEntryProductInfoModel infoModel : configInlineModels)
		{
			infoModel.setOrderEntry(entry);
		}
		getConfigurationPricingOrderIntegrationService().fillSummaryMap(entry);
		getModelService().save(entry);
	}

	protected boolean isConfigurableProduct(final ProductModel product)
	{
		return getCpqConfigurableChecker().isCPQConfiguratorApplicableProduct(product);
	}

	protected ConfigurationAbstractOrderIntegrationStrategy getConfigurationAbstractOrderIntegrationStrategy()
	{
		return configurationAbstractOrderIntegrationStrategy;
	}

	protected CPQConfigurableChecker getCpqConfigurableChecker()
	{
		return this.cpqConfigurableChecker;
	}

	protected ConfigurationOrderEntryProductInfoModelPopulator getConfigInfoPopulator()
	{
		return configInfoPopulator;
	}

	protected ProductConfigurationOrderIntegrationService getConfigurationPricingOrderIntegrationService()
	{
		return configurationPricingOrderIntegrationService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

}
