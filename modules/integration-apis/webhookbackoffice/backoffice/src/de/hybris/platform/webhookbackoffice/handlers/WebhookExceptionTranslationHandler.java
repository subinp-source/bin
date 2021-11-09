/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.webhookbackoffice.handlers;

import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.util.Config;

import java.util.Objects;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;

import com.hybris.cockpitng.service.ExceptionTranslationHandler;

/**
 * Handler that translates exceptions so they appear in the Backoffice
 */
public class WebhookExceptionTranslationHandler implements ExceptionTranslationHandler, Ordered
{
	private static final String SUPPORTED_MODELS = "webhookbackoffice.detailed.error.for.models";
	private static final String DELIMITER = ",";
	private static final String EMPTY_MESSAGE = "";
	private static final int MESSAGE_LENGTH = 2;

	@Override
	public boolean canHandle(final Throwable throwable)
	{
		return Objects.nonNull(throwable) &&
				Objects.nonNull(throwable.getMessage()) &&
				(isExceptionSupported(throwable) || isExceptionSupported(throwable.getCause()));
	}

	@Override
	public String toString(final Throwable throwable)
	{
		Throwable realThrowable = getRealExceptionCause(throwable);
		final String[] message = Objects.toString(realThrowable.getLocalizedMessage(), EMPTY_MESSAGE).split(":", 2);
		return message.length == MESSAGE_LENGTH ? message[1] : realThrowable.getLocalizedMessage();
	}

	@Override
	public int getOrder()
	{
		return Ordered.HIGHEST_PRECEDENCE;
	}

	private Throwable getRealExceptionCause(final Throwable throwable)
	{
		if (throwable.getCause() != null && isExceptionSupported(throwable.getCause()))
		{
			return throwable.getCause();
		}
		return throwable;
	}

	private boolean isExceptionSupported(final Throwable throwable)  // only support ModelSavingException for now.
	{
		return throwable instanceof ModelSavingException && isModelExceptionTranslationSupported(throwable.getMessage());
	}

	private boolean isModelExceptionTranslationSupported(final String exceptionMessage)
	{
		return exceptionMessage != null && readConfiguredModels().anyMatch(exceptionMessage::contains);
	}

	private Stream<String> readConfiguredModels()
	{
		final String property = getConfigParameter();
		if (StringUtils.isNotBlank(property))
		{
			return Stream.of(property.split(DELIMITER));
		}
		else
		{
			return Stream.empty();
		}
	}

	protected String getConfigParameter()
	{
		return Config.getParameter(SUPPORTED_MODELS);
	}
}
