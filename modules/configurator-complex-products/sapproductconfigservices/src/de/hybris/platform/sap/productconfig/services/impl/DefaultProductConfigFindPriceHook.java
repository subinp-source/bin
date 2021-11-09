/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.services.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.order.strategies.calculation.FindPriceHook;
import de.hybris.platform.sap.productconfig.services.intf.ProductConfigurationPricingStrategy;
import de.hybris.platform.util.PriceValue;

import org.apache.log4j.Logger;


/**
 * Product configuration specific implementation of {@link FindPriceHook}
 */
public class DefaultProductConfigFindPriceHook implements FindPriceHook
{
	private static final Logger LOG = Logger.getLogger(DefaultProductConfigFindPriceHook.class);

	private final CPQConfigurableChecker cpqConfigurableChecker;
	private final ProductConfigurationPricingStrategy productConfigurationPricingStrategy;


	public DefaultProductConfigFindPriceHook(final CPQConfigurableChecker cpqConfigurableChecker,
			final ProductConfigurationPricingStrategy productConfigurationPricingStrategy)
	{
		this.cpqConfigurableChecker = cpqConfigurableChecker;
		this.productConfigurationPricingStrategy = productConfigurationPricingStrategy;
	}

	@Override
	public PriceValue findCustomBasePrice(final AbstractOrderEntryModel entry, final PriceValue defaultPrice)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Obtaining base price for configurable product " + entry.getProduct().getCode());
		}
		return getProductConfigurationPricingStrategy().calculateBasePriceForConfiguration(entry);
	}

	@Override
	public boolean isApplicable(final AbstractOrderEntryModel entry)
	{
		return getCpqConfigurableChecker().isCPQConfiguratorApplicableProduct(entry.getProduct());
	}

	protected CPQConfigurableChecker getCpqConfigurableChecker()
	{
		return this.cpqConfigurableChecker;
	}

	protected ProductConfigurationPricingStrategy getProductConfigurationPricingStrategy()
	{
		return productConfigurationPricingStrategy;
	}
}
