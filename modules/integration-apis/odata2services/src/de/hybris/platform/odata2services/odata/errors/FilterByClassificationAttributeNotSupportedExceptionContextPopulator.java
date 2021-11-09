/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.errors;

import de.hybris.platform.integrationservices.exception.FilterByClassificationAttributeNotSupportedException;

import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.processor.ODataErrorContext;

public class FilterByClassificationAttributeNotSupportedExceptionContextPopulator implements ErrorContextPopulator
{
	@Override
	public void populate(@NotNull final ODataErrorContext context)
	{
		final var contextException = context.getException();
		if (contextException instanceof FilterByClassificationAttributeNotSupportedException)
		{
			final var ex = context.getException();
			context.setHttpStatus(HttpStatusCodes.NOT_IMPLEMENTED);
			context.setMessage(ex.getMessage());
			context.setLocale(Locale.ENGLISH);
			context.setErrorCode("filter_not_supported");
		}
	}

	@Override
	public Class<? extends Exception> getExceptionClass()
	{
		return FilterByClassificationAttributeNotSupportedException.class;
	}
}