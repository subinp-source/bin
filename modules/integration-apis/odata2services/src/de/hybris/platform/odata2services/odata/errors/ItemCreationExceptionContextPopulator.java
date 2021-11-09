/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.errors;

import de.hybris.platform.inboundservices.persistence.populator.ItemCreationException;

import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.processor.ODataErrorContext;

/**
 * An {@link ErrorContextPopulator} that provides error values for {@link ItemCreationException}s.
 */
public class ItemCreationExceptionContextPopulator implements ErrorContextPopulator
{
	@Override
	public void populate(@NotNull final ODataErrorContext context)
	{
		final var contextException = context.getException();
		if (contextException instanceof ItemCreationException)
		{
			final var ex = context.getException();
			context.setHttpStatus(HttpStatusCodes.INTERNAL_SERVER_ERROR);
			context.setMessage(ex.getMessage());
			context.setLocale(Locale.ENGLISH);
			context.setErrorCode("internal_error");
		}
	}

	@Override
	public Class<? extends Exception> getExceptionClass()
	{
		return ItemCreationException.class;
	}
}
