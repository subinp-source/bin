/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponservices.sitemsg.process.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customercouponservices.constants.CustomercouponservicesConstants;
import de.hybris.platform.notificationservices.enums.SiteMessageType;
import de.hybris.platform.notificationservices.model.SiteMessageModel;
import de.hybris.platform.notificationservices.processor.Processor;
import de.hybris.platform.notificationservices.strategies.SendSiteMessageStrategy;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Deals with coupon notification for sending site message to the customer
 */
public class CouponNotificationSiteMessageProcessor implements Processor
{

	private static final Logger LOG = Logger.getLogger(CouponNotificationSiteMessageProcessor.class);

	private Map<SiteMessageType, SendSiteMessageStrategy> sendSiteMessageStrategies;

	@Override
	public void process(final CustomerModel customer, final Map<String, ? extends ItemModel> dataMap)
	{
		final SiteMessageModel message = (SiteMessageModel) dataMap.get(CustomercouponservicesConstants.SITE_MESSAGE);

		sendMessage(customer, message);
		LOG.info("Send site message(uid = " + message.getUid() + ")[" + message.getNotificationType() + "] finished");
	}

	protected void sendMessage(final CustomerModel customer, final SiteMessageModel message)
	{
		final SendSiteMessageStrategy strategy = getSendSiteMessageStrategies().get(message.getType());
		if (strategy == null)
		{
			LOG.warn("No SendSiteMessageStrategy found, message[uid=" + message.getUid() + ", type=" + message.getType()
					+ ", notificationType=" + message.getNotificationType() + "] won't be sent.");
			return;
		}
		strategy.sendMessage(customer, message);
	}


	protected Map<SiteMessageType, SendSiteMessageStrategy> getSendSiteMessageStrategies()
	{
		return sendSiteMessageStrategies;
	}

	@Required
	public void setSendSiteMessageStrategies(final Map<SiteMessageType, SendSiteMessageStrategy> sendSiteMessageStrategies)
	{
		this.sendSiteMessageStrategies = sendSiteMessageStrategies;
	}

}
