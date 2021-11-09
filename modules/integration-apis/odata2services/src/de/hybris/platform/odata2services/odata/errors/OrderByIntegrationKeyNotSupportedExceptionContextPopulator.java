/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.errors;

import de.hybris.platform.odata2services.filter.OrderByIntegrationKeyNotSupportedException;

import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.apache.olingo.odata2.api.processor.ODataErrorContext;

/**
 * An {@link ErrorContextPopulator} for handling {@link OrderByIntegrationKeyNotSupportedException}s.
 */
public class OrderByIntegrationKeyNotSupportedExceptionContextPopulator implements ErrorContextPopulator
{

	@Override
	public void populate(@NotNull final ODataErrorContext context)
	{
		if (context.getException() instanceof OrderByIntegrationKeyNotSupportedException)
		{
			final var ex = (OrderByIntegrationKeyNotSupportedException)context.getException();
			context.setMessage(ex.getMessage());
			context.setErrorCode(ex.getCode());
			context.setHttpStatus(ex.getHttpStatus());
			context.setLocale(Locale.ENGLISH);
		}
	}

	@Override
	public Class<? extends Exception> getExceptionClass()
	{
		return OrderByIntegrationKeyNotSupportedException.class;
	}

}
