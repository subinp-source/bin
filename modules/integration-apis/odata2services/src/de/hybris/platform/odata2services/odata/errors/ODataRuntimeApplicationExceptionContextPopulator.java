/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.errors;

import org.apache.olingo.odata2.api.exception.ODataRuntimeApplicationException;
import org.apache.olingo.odata2.api.processor.ODataErrorContext;

/**
 * An {@link ErrorContextPopulator} for handling {@link ODataRuntimeApplicationException}s.
 */
public final class ODataRuntimeApplicationExceptionContextPopulator implements ErrorContextPopulator
{
	@Override
	public void populate(final ODataErrorContext context)
	{
		final var contextException = context.getException();
		if (contextException instanceof ODataRuntimeApplicationException)
		{
			final var exception = (ODataRuntimeApplicationException) contextException;
			context.setHttpStatus(exception.getHttpStatus());
			context.setErrorCode(exception.getCode());
			context.setLocale(exception.getLocale());
			context.setMessage(exception.getMessage());
		}
	}

	@Override
	public Class<ODataRuntimeApplicationException> getExceptionClass()
	{
		return ODataRuntimeApplicationException.class;
	}
}
