/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationservices.service.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.notificationservices.enums.NotificationChannel;
import de.hybris.platform.notificationservices.enums.NotificationType;
import de.hybris.platform.notificationservices.mapping.ProcessorMappingRegistry;
import de.hybris.platform.notificationservices.processor.Processor;
import de.hybris.platform.notificationservices.service.strategies.NotificationChannelStrategy;
import de.hybris.platform.notificationservices.service.strategies.NotificationLanguageStrategy;
import de.hybris.platform.notificationservices.service.strategies.impl.DefaultEmailChannelStrategy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultNotificationServiceUnitTest
{
	private final ProcessorMappingRegistry processorMappingRegistry = new ProcessorMappingRegistry();

	private DefaultNotificationService notificationService;
	private Map<String, ? extends ItemModel> dataMap;
	private CustomerModel customer;
	private Map<String, NotificationLanguageStrategy> notificationLanguageStrategyMap;

	@Mock
	private Map<NotificationChannel, NotificationChannelStrategy> channelStrategies;
	@Mock
	private Processor emailProcessor;
	@Mock
	private Processor smsProcessor;
	@Mock
	private Processor siteMsgProcessor;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		notificationService = new DefaultNotificationService();
		notificationService.setProcessorRegistry(processorMappingRegistry);
		processorMappingRegistry.addMapping(NotificationChannel.EMAIL, NotificationType.NOTIFICATION, emailProcessor);
		processorMappingRegistry.addMapping(NotificationChannel.SMS, NotificationType.NOTIFICATION, smsProcessor);
		processorMappingRegistry.addMapping(NotificationChannel.SITE_MESSAGE, NotificationType.NOTIFICATION, siteMsgProcessor);

		dataMap = new HashMap<>();

		customer = new CustomerModel();
		final Set<NotificationChannel> preferences = new HashSet();
		preferences.add(NotificationChannel.EMAIL);
		preferences.add(NotificationChannel.SMS);
		preferences.add(NotificationChannel.SITE_MESSAGE);
		customer.setNotificationChannels(preferences);


		notificationLanguageStrategyMap = new HashMap<>();
		notificationService.setNotificationLanguageStrategyMap(notificationLanguageStrategyMap);

		notificationService.setNotificationChannelStrategyMap(channelStrategies);
	}

	@Test
	public void testNotifyCustomerByNotAssignedChannelSet()
	{
		notificationService.notifyCustomer(NotificationType.NOTIFICATION, customer, dataMap);

		verify(emailProcessor, times(1)).process(customer, dataMap);
		verify(smsProcessor, times(1)).process(customer, dataMap);
		verify(siteMsgProcessor, times(1)).process(customer, dataMap);
	}

	@Test
	public void testNotifyCustomerByAssignedChannelSet()
	{
		final Set<NotificationChannel> channelSet = new HashSet<NotificationChannel>();
		channelSet.add(NotificationChannel.EMAIL);
		notificationService.notifyCustomer(NotificationType.NOTIFICATION, customer, channelSet, dataMap);
		verify(emailProcessor, times(1)).process(customer, dataMap);
	}


	@Test
	public void testGetChannelValueForEmail()
	{
		final CustomerModel customer = mock(CustomerModel.class);
		final String mail = "test@hybris.com";
		customer.setUid(mail);
		final NotificationChannelStrategy emailStrategy = mock(DefaultEmailChannelStrategy.class);
		channelStrategies.put(NotificationChannel.EMAIL, emailStrategy);

		when(customer.getContactEmail()).thenReturn(mail);
		when(channelStrategies.get(NotificationChannel.EMAIL)).thenReturn(emailStrategy);
		when(emailStrategy.getChannelValue(customer)).thenReturn(mail);

		Assert.assertEquals(mail, notificationService.getChannelValue(NotificationChannel.EMAIL, customer));
		verify(channelStrategies).get(NotificationChannel.EMAIL);
	}


	@Test
	public void testGetChannelValueFromSiteMsg()
	{
		final CustomerModel customer = new CustomerModel();

		Assert.assertNull(notificationService.getChannelValue(NotificationChannel.SITE_MESSAGE, customer));
		verify(channelStrategies).get(NotificationChannel.SITE_MESSAGE);
	}
}
