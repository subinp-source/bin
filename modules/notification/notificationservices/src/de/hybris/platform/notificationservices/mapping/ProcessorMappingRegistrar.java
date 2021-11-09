/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationservices.mapping;

import de.hybris.platform.notificationservices.enums.NotificationChannel;
import de.hybris.platform.notificationservices.enums.NotificationType;
import de.hybris.platform.notificationservices.processor.Processor;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Required;


/**
 * register one processor to one specific registry
 */
public class ProcessorMappingRegistrar
{
	private ProcessorMappingRegistry registry;
	private NotificationType notificationType;
	private NotificationChannel notificationChannel;
	private Processor processor;

	protected ProcessorMappingRegistry getRegistry()
	{
		return registry;
	}

	@Required
	public void setRegistry(final ProcessorMappingRegistry registry)
	{
		this.registry = registry;
	}

	@Required
	public void setNotificationType(final NotificationType notificationType)
	{
		this.notificationType = notificationType;
	}

	@Required
	public void setProcessor(final Processor processor)
	{
		this.processor = processor;
	}

	@Required
	public void setNotificationChannel(NotificationChannel notificationChannel)
	{
		this.notificationChannel = notificationChannel;
	}


	/**
	 * add new processor mapped with one specific notification type to one channel registry
	 */
	@PostConstruct
	public void registerMapping()
	{
		registry.addMapping(notificationChannel, notificationType, processor);
	}
}
