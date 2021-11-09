/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.webhookservices.service;

import de.hybris.platform.apiregistryservices.dto.EventSourceData;

import javax.validation.constraints.NotNull;

/**
 * A service to send an event to the webhook
 */
public interface WebhookEventSender
{
	/**
	 * Sends the given event
	 * 
	 * @param eventSourceData Context of the event to be sent
	 */
	void send(@NotNull EventSourceData eventSourceData);

}
