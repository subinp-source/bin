/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.facades.impl;

import de.hybris.platform.sap.productconfig.facades.ConfigurationData;
import de.hybris.platform.sap.productconfig.facades.ConfigurationMessageMapper;
import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.CsticValueData;
import de.hybris.platform.sap.productconfig.facades.ProductConfigMessageData;
import de.hybris.platform.sap.productconfig.facades.ProductConfigMessageUISeverity;
import de.hybris.platform.sap.productconfig.facades.ValueFormatTranslator;
import de.hybris.platform.sap.productconfig.facades.overview.ConfigurationOverviewData;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ProductConfigMessage;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ProductConfigMessageSeverity;
import de.hybris.platform.sap.productconfig.services.intf.ProductConfigurationService;
import de.hybris.platform.sap.productconfig.services.strategies.lifecycle.intf.ConfigurationAbstractOrderEntryLinkStrategy;
import de.hybris.platform.servicelayer.internal.service.ServicelayerUtils;
import de.hybris.platform.util.localization.Localization;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.sap.security.core.server.csi.XSSEncoder;


/**
 * Helper class to map the messages from product configuration(product level), cstic and cstic values
 */
public class ConfigurationMessageMapperImpl implements ConfigurationMessageMapper
{
	private static final Logger LOG = Logger.getLogger(ConfigurationMessageMapperImpl.class);
	protected static final String UNRESOLVABLE_ISSUES_MSG = "sapproductconfig.unresolvable.issues.msg";
	private ValueFormatTranslator valueFormatTranslator;
	private ConfigurationAbstractOrderEntryLinkStrategy configurationAbstractOrderEntryLinkStrategy;
	private ProductConfigurationService productConfigurationService;


	@Override
	public void mapMessagesFromModelToData(final CsticData cstciData, final CsticModel csticModel)
	{
		final Set<ProductConfigMessage> modelMessages = csticModel.getMessages();
		final List<ProductConfigMessageData> uiMessages = mapMessagesFromModelToData(modelMessages);
		uiMessages.sort(new ConfigurationMessageComparator());
		cstciData.setMessages(uiMessages);
	}

	@Override
	public void mapMessagesFromModelToData(final CsticValueData cstciValueData, final CsticValueModel csticValueModel)
	{
		final Set<ProductConfigMessage> modelMessages = csticValueModel.getMessages();
		final List<ProductConfigMessageData> uiMessages = mapMessagesFromModelToData(modelMessages);
		uiMessages.sort(new ConfigurationMessageComparator());
		cstciValueData.setMessages(uiMessages);
	}

	@Override
	public void mapMessagesFromModelToData(final ConfigurationData configData, final ConfigModel configModel)
	{
		final Set<ProductConfigMessage> modelMessages = configModel.getMessages();
		final List<ProductConfigMessageData> uiMessages = mapMessagesFromModelToData(modelMessages);
		handleMessageForUnresolvableIssues(configModel, uiMessages);
		configData.setMessages(uiMessages);
	}

	@Override
	public void mapMessagesFromModelToData(final ConfigurationOverviewData configOverviewData, final ConfigModel configModel)
	{
		final Set<ProductConfigMessage> modelMessages = configModel.getMessages();
		final List<ProductConfigMessageData> uiMessages = mapMessagesFromModelToData(modelMessages);
		handleOverviewMessageForUnresolvableIssues(configModel, uiMessages);
		configOverviewData.setMessages(uiMessages);
	}

	protected void handleOverviewMessageForUnresolvableIssues(final ConfigModel configModel,
			final List<ProductConfigMessageData> messages)
	{
		if (isIncompleteConfig(configModel))
		{
			addUnresolvableIssuesMessage(messages);
		}
	}

	protected void handleMessageForUnresolvableIssues(final ConfigModel configModel, final List<ProductConfigMessageData> messages)
	{
		if (isIncompleteDraftConfig(configModel))
		{
			addUnresolvableIssuesMessage(messages);
		}
	}

	protected void addUnresolvableIssuesMessage(final List<ProductConfigMessageData> messages)
	{
		final ProductConfigMessageData message = createMessageForUnresolvableIssues();
		if (null != message)
		{
			messages.add(message);
		}
	}

	protected boolean isIncompleteConfig(final ConfigModel configModel)
	{
		return (!configModel.isComplete() || !configModel.isConsistent())
				&& 0 == getProductConfigurationService().getTotalNumberOfIssues(configModel);
	}

	protected boolean isIncompleteDraftConfig(final ConfigModel configModel)
	{
		return null != getConfigurationAbstractOrderEntryLinkStrategy().getCartEntryForDraftConfigId(configModel.getId())
				&& isIncompleteConfig(configModel);
	}

	protected ProductConfigMessageData createMessageForUnresolvableIssues()
	{
		ProductConfigMessageData message = null;
		try
		{
			message = new ProductConfigMessageData();
			message.setMessage(encodeHTML(getLocalizedText(UNRESOLVABLE_ISSUES_MSG)));
			message.setSeverity(ProductConfigMessageUISeverity.ERROR);
		}
		catch (final UnsupportedEncodingException ex)
		{
			LOG.warn("Message discarded due to unsupported encoding: " + ex.getMessage(), ex);
		}
		return message;
	}

	protected String getLocalizedText(final String key)
	{
		if (ServicelayerUtils.isSystemInitialized())
		{
			return Localization.getLocalizedString(key);
		}
		else
		{
			LOG.warn("Localized texts are not retrieved - this is ok in unit test mode");
			return key;
		}
	}

	protected List<ProductConfigMessageData> mapMessagesFromModelToData(final Set<ProductConfigMessage> modelMessages)
	{
		final List<ProductConfigMessageData> uiMessages = new ArrayList(modelMessages.size());
		for (final ProductConfigMessage message : modelMessages)
		{
			uiMessages.add(createUiMessage(message));
		}
		return uiMessages;
	}

	protected ProductConfigMessageData createUiMessage(final ProductConfigMessage message)
	{
		final ProductConfigMessageData uiMessage = new ProductConfigMessageData();
		try
		{
			uiMessage.setMessage(encodeHTML(message.getMessage()));
			if (message.getExtendedMessage() != null)
			{
				uiMessage.setExtendedMessage(encodeHTML(message.getExtendedMessage()));
			}
		}
		catch (final UnsupportedEncodingException ex)
		{
			uiMessage.setMessage(null);
			LOG.warn("Message with key '" + message.getKey() + "' discarded due to unsupported encoding: " + ex.getMessage(), ex);
		}
		uiMessage.setSeverity(mapMessageSeverity(message));
		uiMessage.setPromoType(message.getPromoType());
		uiMessage.setEndDate(getValueFormatTranslator().formatDate(message.getEndDate()));
		return uiMessage;
	}

	protected String encodeHTML(final String message) throws UnsupportedEncodingException
	{
		return XSSEncoder.encodeHTML(message);
	}

	protected ProductConfigMessageUISeverity mapMessageSeverity(final ProductConfigMessage message)
	{
		final ProductConfigMessageUISeverity severity;
		if (ProductConfigMessageSeverity.WARNING.equals(message.getSeverity()))
		{
			// WARNING -> INFO (there is no warning level in the UI)
			severity = ProductConfigMessageUISeverity.INFO;
		}
		else if (ProductConfigMessageSeverity.INFO.equals(message.getSeverity()))
		{
			// INFO -> CONFIG
			severity = ProductConfigMessageUISeverity.CONFIG;
		}
		else
		{
			// for example ERROR -> ERROR
			severity = ProductConfigMessageUISeverity.valueOf(message.getSeverity().toString());
		}
		return severity;
	}

	/**
	 * @return the valueFormatTranslator
	 */
	public ValueFormatTranslator getValueFormatTranslator()
	{
		return valueFormatTranslator;
	}

	/**
	 * @param valueFormatTranslator
	 *           the valueFormatTranslator to set
	 */
	@Required
	public void setValueFormatTranslator(final ValueFormatTranslator valueFormatTranslator)
	{
		this.valueFormatTranslator = valueFormatTranslator;
	}

	protected ConfigurationAbstractOrderEntryLinkStrategy getConfigurationAbstractOrderEntryLinkStrategy()
	{
		return configurationAbstractOrderEntryLinkStrategy;
	}

	@Required
	public void setConfigurationAbstractOrderEntryLinkStrategy(
			final ConfigurationAbstractOrderEntryLinkStrategy configurationAbstractOrderEntryLinkStrategy)
	{
		this.configurationAbstractOrderEntryLinkStrategy = configurationAbstractOrderEntryLinkStrategy;
	}

	protected ProductConfigurationService getProductConfigurationService()
	{
		return productConfigurationService;
	}

	@Required
	public void setProductConfigurationService(final ProductConfigurationService productConfigurationService)
	{
		this.productConfigurationService = productConfigurationService;
	}

}
