/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.services.strategies.impl;

import de.hybris.platform.commerceservices.enums.QuoteAction;
import de.hybris.platform.commerceservices.order.exceptions.IllegalQuoteStateException;
import de.hybris.platform.commerceservices.order.strategies.QuoteActionValidationStrategy;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.services.data.QuoteEntryStatus;
import de.hybris.platform.sap.productconfig.services.impl.CPQConfigurableChecker;
import de.hybris.platform.sap.productconfig.services.strategies.lifecycle.intf.ConfigurationAbstractOrderIntegrationStrategy;
import de.hybris.platform.servicelayer.internal.service.ServicelayerUtils;
import de.hybris.platform.util.localization.Localization;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.sap.security.core.server.csi.XSSEncoder;


public class ProductConfigurationQuoteActionValidationStrategyImpl implements QuoteActionValidationStrategy
{

	private QuoteActionValidationStrategy defaultQuoteActionValidationStrategy;
	private ConfigurationAbstractOrderIntegrationStrategy configurationAbstractOrderIntegrationStrategy;
	private CPQConfigurableChecker cpqConfigurableChecker;
	private static final Logger LOG = Logger.getLogger(ProductConfigurationQuoteActionValidationStrategyImpl.class);


	protected CPQConfigurableChecker getCpqConfigurableChecker()
	{
		return cpqConfigurableChecker;
	}

	protected ConfigurationAbstractOrderIntegrationStrategy getConfigurationAbstractOrderIntegrationStrategy()
	{
		return configurationAbstractOrderIntegrationStrategy;
	}

	protected QuoteActionValidationStrategy getDefaultQuoteActionValidationStrategy()
	{
		return defaultQuoteActionValidationStrategy;
	}

	/**
	 * @param quoteActionValidationStrategy
	 *           Default strategy (from commerceservices or extensions on top)
	 */
	@Required
	public void setDefaultQuoteActionValidationStrategy(final QuoteActionValidationStrategy quoteActionValidationStrategy)
	{
		this.defaultQuoteActionValidationStrategy = quoteActionValidationStrategy;
	}

	/**
	 * @param configurationAbstractOrderIntegrationStrategy
	 *           Strategy for accessing configurations attached to abstract order entries
	 */
	@Required
	public void setConfigurationAbstractOrderIntegrationStrategy(
			final ConfigurationAbstractOrderIntegrationStrategy configurationAbstractOrderIntegrationStrategy)
	{
		this.configurationAbstractOrderIntegrationStrategy = configurationAbstractOrderIntegrationStrategy;

	}

	@Override
	public void validate(final QuoteAction quoteAction, final QuoteModel quoteModel, final UserModel userModel)
	{
		if (quoteAction.equals(QuoteAction.CHECKOUT))
		{
			final Optional<QuoteEntryStatus> singleConfigurationIssue = getSingleConfigurationIssue(quoteModel);
			if (singleConfigurationIssue.isPresent())
			{
				final String localizedMessage = getLocalizedText(singleConfigurationIssue.get().getProductCode());
				throw new IllegalQuoteStateException(quoteAction, quoteModel.getCode(), quoteModel.getState(),
						quoteModel.getVersion(), localizedMessage, true);
			}
		}
		getDefaultQuoteActionValidationStrategy().validate(quoteAction, quoteModel, userModel);

	}

	protected String getLocalizedText(final String productCode)
	{
		if (ServicelayerUtils.isSystemInitialized())
		{
			try
			{
				return XSSEncoder.encodeHTML(Localization.getLocalizedString("sapproductconfig.quote.issues.msg", new String[]
				{ productCode }));
			}
			catch (final UnsupportedEncodingException e)
			{
				throw new IllegalStateException("Could not encode", e);
			}
		}
		else
		{
			LOG.warn("Localized texts are not retrieved - this is ok in unit test mode");
			return productCode;
		}
	}

	@Required
	public void setCpqConfigurableChecker(final CPQConfigurableChecker cpqConfigurableChecker)
	{
		this.cpqConfigurableChecker = cpqConfigurableChecker;

	}

	@Override
	public boolean isValidAction(final QuoteAction quoteAction, final QuoteModel quoteModel, final UserModel userModel)
	{

		return getDefaultQuoteActionValidationStrategy().isValidAction(quoteAction, quoteModel, userModel);
	}


	protected QuoteEntryStatus getQuoteEntryStatus(final AbstractOrderEntryModel entry)
	{
		final QuoteEntryStatus quoteEntryStatus = new QuoteEntryStatus();
		quoteEntryStatus.setProductCode(entry.getProduct().getCode());
		quoteEntryStatus.setHasConfigurationIssue(hasConfigurationIssue(entry));
		return quoteEntryStatus;

	}

	protected boolean hasConfigurationIssue(final AbstractOrderEntryModel quoteEntry)
	{
		if (getCpqConfigurableChecker().isCPQConfiguratorApplicableProduct(quoteEntry.getProduct()))
		{
			final ConfigModel configurationModel = getConfigurationAbstractOrderIntegrationStrategy()
					.getConfigurationForAbstractOrderEntryForOneTimeAccess(quoteEntry);
			return (!configurationModel.isComplete()) || (!configurationModel.isConsistent());
		}
		else
		{
			return false;
		}
	}

	protected Optional<QuoteEntryStatus> getSingleConfigurationIssue(final QuoteModel quoteModel)
	{
		return quoteModel.getEntries().stream().map(entry -> getQuoteEntryStatus(entry))
				.filter(entry -> entry.getHasConfigurationIssue()).findFirst();
	}





}
