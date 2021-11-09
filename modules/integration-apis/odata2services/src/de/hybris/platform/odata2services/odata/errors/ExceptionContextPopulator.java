/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.errors;

import java.util.Locale;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.processor.ODataErrorContext;

/**
 * An {@link ErrorContextPopulator} that provides context error values for any {@link Exception}s and is called when no more specific
 * providers found registered in {@link CustomODataExceptionAwareErrorCallback}.
 */
public final class ExceptionContextPopulator implements ErrorContextPopulator
{
	private static final String DEFAULT_MESSAGE = "An unexpected error occurred. See the log for details";

	@Override
	public void populate(final ODataErrorContext context)
	{
		context.setHttpStatus(HttpStatusCodes.INTERNAL_SERVER_ERROR);
		context.setErrorCode("unknown_error");
		context.setMessage(DEFAULT_MESSAGE);
		context.setLocale(Locale.ENGLISH);
	}

	@Override
	public Class<Exception> getExceptionClass()
	{
		return Exception.class;
	}
}
