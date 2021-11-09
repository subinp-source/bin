/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.stocknotificationservices.sms.processor.impl;

import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.notificationservices.enums.NotificationChannel;
import de.hybris.platform.notificationservices.processor.Processor;
import de.hybris.platform.notificationservices.service.NotificationService;
import de.hybris.platform.notificationservices.strategies.SendSmsMessageStrategy;
import de.hybris.platform.stocknotificationservices.constants.StocknotificationservicesConstants;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.util.localization.Localization;

import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Processor to send back-in-stock notification via SMS
 */
public class DefaultStockNotificationSmsProcessor implements Processor
{
	private static final Logger LOG = Logger.getLogger(DefaultStockNotificationSmsProcessor.class.getName());

	private SendSmsMessageStrategy sendSmsMessageStrategy;
	private NotificationService notificationService;
	private CommerceStockService commerceStockService;

	@Override
	public void process(final CustomerModel customer, final Map<String, ? extends ItemModel> dataMap)
	{
		final ProductModel product = (ProductModel) dataMap.get(StocknotificationservicesConstants.PRODUCT);
		final BaseStoreModel currentBaseStore = (BaseStoreModel) dataMap.get(StocknotificationservicesConstants.BASE_STORE);
		final String message;
		if (Objects.nonNull(getProductStockLevel(product, currentBaseStore)))
		{
			message = Localization.getLocalizedString("sms.product.back_in_stock", new Object[]
			{ product.getName(), getProductStockLevel(product, currentBaseStore) });
		}
		else
		{
			message = Localization.getLocalizedString("sms.product.back_in_stock.forceInStock", new Object[]
			{ product.getName() });
		}
		final String phoneNumber = getNotificationService().getChannelValue(NotificationChannel.SMS, customer);
		if (StringUtils.isEmpty(phoneNumber))
		{
			LOG.warn("No phone number found for customer, message[" + message + "] will not be sent.");
			return;
		}

		getSendSmsMessageStrategy().sendMessage(phoneNumber, message);
	}

	protected Long getProductStockLevel(final ProductModel product, final BaseStoreModel baseStore)
	{
		return getCommerceStockService().getStockLevelForProductAndBaseStore(product, baseStore);
	}

	protected NotificationService getNotificationService()
	{
		return notificationService;
	}

	@Required
	public void setNotificationService(final NotificationService notificationService)
	{
		this.notificationService = notificationService;
	}

	protected SendSmsMessageStrategy getSendSmsMessageStrategy()
	{
		return sendSmsMessageStrategy;
	}

	@Required
	public void setSendSmsMessageStrategy(final SendSmsMessageStrategy sendSmsMessageStrategy)
	{
		this.sendSmsMessageStrategy = sendSmsMessageStrategy;
	}

	protected CommerceStockService getCommerceStockService()
	{
		return commerceStockService;
	}

	@Required
	public void setCommerceStockService(final CommerceStockService commerceStockService)
	{
		this.commerceStockService = commerceStockService;
	}

}
