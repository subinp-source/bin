/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.dataexport.generic.event;

import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.event.events.MessageSendingEventListener;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.ErrorHandler;


/**
 * Event listener that will handle exceptions that occur further down the pipeline. Will only work for synchronous calls
 * i.e. queues wont work.
 */
public class ExportMessageSendingEventListener extends MessageSendingEventListener
{
	private ErrorHandler errorHandler;

	public ErrorHandler getErrorHandler()
	{
		return errorHandler;
	}

	@Required
	public void setErrorHandler(final ErrorHandler errorHandler)
	{
		this.errorHandler = errorHandler;
	}

	@Override
	protected void send(final AbstractEvent event)
	{
		try
		{
			super.send(event);
		}
		catch (final Exception e)
		{
			errorHandler.handleError(e);
		}
	}
}
