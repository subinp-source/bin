/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.services;

import de.hybris.platform.apiregistryservices.dto.EventExportDeadLetterData;


/**
 * Service layer interface for DLQ.
 */
public interface EventDlqService
{
	/**
	 * Send EventExportDeadLetterData to DLQ.
	 *
	 * @param data
	 *           the data which stores event details, export requestBody, error response.
	 */
	void sendToQueue(EventExportDeadLetterData data);
}
