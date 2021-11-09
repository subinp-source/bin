/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.errors;

import de.hybris.platform.integrationservices.exception.LocaleNotSupportedException;

import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.processor.ODataErrorContext;

/**
 * An {@link ErrorContextPopulator} that provides error values for {@link LocaleNotSupportedException}s.
 */
public class LocaleNotSupportedExceptionContextPopulator implements ErrorContextPopulator
{
	private static final String ERROR_CODE = "invalid_language";

	@Override
	public void populate(@NotNull final ODataErrorContext context)
	{
		final var contextException = context.getException();
		if (contextException instanceof LocaleNotSupportedException)
		{
			final var ex = (LocaleNotSupportedException)context.getException();
			context.setHttpStatus(HttpStatusCodes.BAD_REQUEST);
			context.setMessage(ex.getMessage());
			context.setErrorCode(ERROR_CODE);
			context.setLocale(Locale.ENGLISH);
			context.setInnerError(ex.getIntegrationKey());
		}
	}

	@Override
	public Class<LocaleNotSupportedException> getExceptionClass()
	{
		return LocaleNotSupportedException.class;
	}
}
