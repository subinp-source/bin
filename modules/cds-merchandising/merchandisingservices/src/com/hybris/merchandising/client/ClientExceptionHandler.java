/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.client;

import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hybris.charon.exp.HttpException;
import com.hybris.charon.exp.NotFoundException;

import rx.Observable;

public final class ClientExceptionHandler
{

	private static final Logger LOG = LoggerFactory.getLogger(ClientExceptionHandler.class);
	private static final String NO_SERVER_MESSAGE = "No server message available";
	private static final String ERROR_IN_CLASS = "Error in class";

	private ClientExceptionHandler()
	{
		//empty to avoid instantiating this class
	}

	public static void handleExceptions(final Throwable ex, Class<?> clazz)
	{
		if (ex instanceof NotFoundException)
		{
			throw new UnknownIdentifierException(ex.getMessage(), ex);
		}
		if (ex instanceof HttpException)
		{
			getServerMessage((HttpException) ex, clazz);
		}
	}

	private static void getServerMessage(final HttpException ex, Class<?> clazz)
	{
		final Observable<String> serverMessage = ex.getServerMessage();
		if (serverMessage == null)
		{
			LOG.error("{} {} {}", ERROR_IN_CLASS, clazz.getName(), NO_SERVER_MESSAGE);
		}
		else {
			serverMessage.subscribe(errorMessage -> {
				LOG.error("{} {} {}", ERROR_IN_CLASS, clazz.getName(), errorMessage);
			});
		}
	}

}