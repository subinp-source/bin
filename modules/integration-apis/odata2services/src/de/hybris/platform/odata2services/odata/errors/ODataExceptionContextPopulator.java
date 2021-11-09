/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.errors;

import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.exception.ODataRuntimeApplicationException;
import org.apache.olingo.odata2.api.processor.ODataErrorContext;
import org.springframework.beans.factory.annotation.Required;

/**
 * An {@link ErrorContextPopulator} providing error values for {@link ODataException}s
 */
public final class ODataExceptionContextPopulator implements ErrorContextPopulator
{
	ErrorContextPopulator runtimeExceptionPopulator;

	@Override
	public void populate(final ODataErrorContext context)
	{
		final var exception = context.getException().getCause();
		if (exception instanceof ODataRuntimeApplicationException)
		{
			context.setException((ODataRuntimeApplicationException) exception);
			runtimeExceptionPopulator.populate(context);
		}
	}

	@Override
	public Class<ODataException> getExceptionClass()
	{
		return ODataException.class;
	}

	@Required
	public void setRuntimeExceptionContextPopulator(final ErrorContextPopulator callback)
	{
		runtimeExceptionPopulator = callback;
	}
}
