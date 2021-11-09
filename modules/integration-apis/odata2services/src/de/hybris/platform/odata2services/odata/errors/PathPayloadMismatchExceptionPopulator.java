/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.errors;

import de.hybris.platform.odata2services.odata.processor.handler.persistence.PathPayloadKeyMismatchException;

import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.processor.ODataErrorContext;

/**
 * Populates the error context with information from {@link PathPayloadKeyMismatchException}
 */
public class PathPayloadMismatchExceptionPopulator implements ErrorContextPopulator
{
	@Override
	public void populate(@NotNull final ODataErrorContext context)
	{
		final var contextException = context.getException();
		if (contextException instanceof PathPayloadKeyMismatchException)
		{
			final var exception = (PathPayloadKeyMismatchException) contextException;
			context.setHttpStatus(HttpStatusCodes.BAD_REQUEST);
			context.setErrorCode("invalid_key");
			context.setLocale(Locale.ENGLISH);
			context.setMessage(exception.getMessage());
		}
	}

	@Override
	public Class<? extends Exception> getExceptionClass()
	{
		return PathPayloadKeyMismatchException.class;
	}
}
