/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.errors;

import de.hybris.platform.odata2services.odata.persistence.PersistenceRuntimeApplicationException;

import org.apache.olingo.odata2.api.processor.ODataErrorContext;

/**
 * An {@link ErrorContextPopulator} providing error values for {@link PersistenceRuntimeApplicationException}s
 */
public final class PersistenceRuntimeApplicationExceptionContextPopulator implements ErrorContextPopulator
{
	@Override
	public void populate(final ODataErrorContext context)
	{
		final var contextException = context.getException();
		if (contextException instanceof PersistenceRuntimeApplicationException)
		{
			final var exception = (PersistenceRuntimeApplicationException) contextException;
			context.setHttpStatus(exception.getHttpStatus());
			context.setErrorCode(exception.getCode());
			context.setLocale(exception.getLocale());
			context.setMessage(exception.getMessage());
			context.setInnerError(exception.getIntegrationKey());
		}
	}

	@Override
	public Class<PersistenceRuntimeApplicationException> getExceptionClass()
	{
		return PersistenceRuntimeApplicationException.class;
	}
}
