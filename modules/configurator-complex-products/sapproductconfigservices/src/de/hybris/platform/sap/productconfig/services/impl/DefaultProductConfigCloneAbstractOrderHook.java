/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.services.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.order.strategies.ordercloning.CloneAbstractOrderHook;
import de.hybris.platform.sap.productconfig.services.strategies.lifecycle.intf.ConfigurationAbstractOrderEntryLinkStrategy;
import de.hybris.platform.sap.productconfig.services.strategies.lifecycle.intf.ConfigurationAbstractOrderIntegrationStrategy;
import de.hybris.platform.sap.productconfig.services.strategies.lifecycle.intf.ConfigurationCopyStrategy;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


public class DefaultProductConfigCloneAbstractOrderHook implements CloneAbstractOrderHook
{

	private static final Logger LOG = Logger.getLogger(DefaultProductConfigCloneAbstractOrderHook.class);

	private final ConfigurationAbstractOrderEntryLinkStrategy configurationAbstractOrderEntryLinkStrategy;
	private final ConfigurationCopyStrategy configCopyStrategy;
	private final ConfigurationAbstractOrderIntegrationStrategy configurationAbstractOrderIntegrationStrategy;

	/**
	 * default constructor
	 *
	 * @param configurationAbstractOrderEntryLinkStrategy
	 * @param configurationAbstractOrderIntegrationStrategy
	 * @param configCopyStrategy
	 */
	public DefaultProductConfigCloneAbstractOrderHook(
			final ConfigurationAbstractOrderEntryLinkStrategy configurationAbstractOrderEntryLinkStrategy,
			final ConfigurationAbstractOrderIntegrationStrategy configurationAbstractOrderIntegrationStrategy,
			final ConfigurationCopyStrategy configCopyStrategy)
	{
		super();
		this.configurationAbstractOrderEntryLinkStrategy = configurationAbstractOrderEntryLinkStrategy;
		this.configurationAbstractOrderIntegrationStrategy = configurationAbstractOrderIntegrationStrategy;
		this.configCopyStrategy = configCopyStrategy;
	}



	@Override
	public void beforeClone(final AbstractOrderModel original, final Class abstractOrderClassResult)
	{
		// nothing to do here
	}


	@Override
	public <T extends AbstractOrderModel> void afterClone(final AbstractOrderModel original, final T clone,
			final Class abstractOrderClassResult)
	{
		//Whenever a cart is cloned into a quote: release all configuration sessions attached to the cart
		if (isCleanUpNeeded(original, abstractOrderClassResult))
		{
			cleanUp(original);
		}
		// Execute additional steps to finalize clone process whenever:
		//  - a cart, quote or an order is cloned into a cart
		//  - a quote is cloned into a quote
		if (isFinalizeCloneNeeded(original, abstractOrderClassResult))
		{
			getConfigCopyStrategy().finalizeClone(original, clone);
		}
		else
		{
			//perform shallow copy
			getConfigCopyStrategy().finalizeCloneShallowCopy(original, clone);
		}
		if (LOG.isDebugEnabled())
		{
			traceCPQAspects(clone);
		}
	}

	@Override
	public void beforeCloneEntries(final AbstractOrderModel original)
	{
		//Whenever a cart is cloned into a quote: release all configuration sessions attached to the cart
		if (isCleanUpNeeded(original))
		{
			cleanUp(original);
		}
	}


	@Override
	public <T extends AbstractOrderEntryModel> void afterCloneEntries(final AbstractOrderModel original,
			final List<T> clonedEntries)
	{
		// nothing to do here
	}

	protected boolean isFinalizeCloneNeeded(final AbstractOrderModel original, final Class abstractOrderClassResult)
	{
		return isQuoteOrOrderOrCartToCartCloneProcess(original, abstractOrderClassResult)
				|| isQuoteToQuoteCloneProcess(original, abstractOrderClassResult);
	}

	protected boolean isQuoteOrOrderOrCartToCartCloneProcess(final AbstractOrderModel original,
			final Class abstractOrderClassResult)
	{
		return (CartModel.class.isAssignableFrom(abstractOrderClassResult)
				&& (original instanceof QuoteModel || original instanceof OrderModel || original instanceof CartModel));
	}

	protected boolean isQuoteToQuoteCloneProcess(final AbstractOrderModel original, final Class abstractOrderClassResult)
	{
		return (QuoteModel.class.isAssignableFrom(abstractOrderClassResult) && original instanceof QuoteModel);
	}

	protected void traceCPQAspects(final AbstractOrderModel orderModel)
	{
		LOG.debug("After clone, target document has code: " + orderModel.getCode());
		orderModel.getEntries().stream().forEach(entry -> traceCPQAspects(entry));
	}

	protected void traceCPQAspects(final AbstractOrderEntryModel entry)
	{
		LOG.debug("Product configuration: " + entry.getProductConfiguration() + " for entry " + entry.getPk());
	}

	protected boolean isCleanUpNeeded(final AbstractOrderModel original, final Class abstractOrderClassResult)
	{
		return QuoteModel.class.isAssignableFrom(abstractOrderClassResult) && original instanceof CartModel;
	}

	protected boolean isCleanUpNeeded(final AbstractOrderModel original)
	{
		return original instanceof CartModel;
	}

	protected void cleanUp(final AbstractOrderModel original)
	{
		if (original == null)
		{
			throw new IllegalArgumentException("Abstract Order to clean up must not be null");
		}
		original.getEntries().stream().forEach(entry -> cleanUpEntry(entry));
	}

	protected void cleanUpEntry(final AbstractOrderEntryModel entry)
	{
		final PK pk = entry.getPk();
		if (pk != null)
		{
			final String cartEntryKey = pk.toString();
			final String configId = getConfigurationAbstractOrderEntryLinkStrategy().getConfigIdForCartEntry(cartEntryKey);
			if (!StringUtils.isEmpty(configId))
			{
				getConfigurationAbstractOrderIntegrationStrategy().finalizeCartEntry(entry);
			}
		}
	}

	protected ConfigurationAbstractOrderEntryLinkStrategy getConfigurationAbstractOrderEntryLinkStrategy()
	{
		return configurationAbstractOrderEntryLinkStrategy;
	}

	protected ConfigurationCopyStrategy getConfigCopyStrategy()
	{
		return configCopyStrategy;
	}

	protected ConfigurationAbstractOrderIntegrationStrategy getConfigurationAbstractOrderIntegrationStrategy()
	{
		return configurationAbstractOrderIntegrationStrategy;
	}


}
