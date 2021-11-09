/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.stocknotificationservices.sitemsg.processor.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.notificationservices.enums.SiteMessageType;
import de.hybris.platform.notificationservices.model.SiteMessageModel;
import de.hybris.platform.notificationservices.strategies.SendSiteMessageStrategy;
import de.hybris.platform.notificationservices.strategies.impl.DefaultSendSiteMessageStrategy;
import de.hybris.platform.stocknotificationservices.constants.StocknotificationservicesConstants;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class StockNotificationSiteMessageProcessorTest
{
	private StockNotificationSiteMessageProcessor processor;
	@Mock
	private Map<SiteMessageType, SendSiteMessageStrategy> sendSiteMessageStrategies;
	@Mock
	private Map<String, ? extends ItemModel> dataMap;
	@Mock
	private CustomerModel customer;
	@Mock
	private SiteMessageModel message;
	@Mock
	private DefaultSendSiteMessageStrategy strategy;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		processor = new StockNotificationSiteMessageProcessor();
		processor.setSendSiteMessageStrategies(sendSiteMessageStrategies);
		Mockito.when((SiteMessageModel) dataMap.get(StocknotificationservicesConstants.SITE_MESSAGE)).thenReturn(message);
		Mockito.when(sendSiteMessageStrategies.get(message.getType())).thenReturn(strategy);
		Mockito.doNothing().when(strategy).sendMessage(customer, message);
	}

	@Test
	public void test_process()
	{
		processor.process(customer, dataMap);
		Mockito.verify(strategy, Mockito.times(1)).sendMessage(customer, message);
	}

	@Test
	public void test_process_messageNull()
	{
		Mockito.when(sendSiteMessageStrategies.get(message.getType())).thenReturn(null);
		processor.process(customer, dataMap);
		Mockito.verify(strategy, Mockito.times(0)).sendMessage(customer, message);
	}
}
