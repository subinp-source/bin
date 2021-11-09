/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.errors;

import de.hybris.platform.inboundservices.persistence.CannotCreateReferencedItemException;

import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.processor.ODataErrorContext;

/**
 * An {@link ErrorContextPopulator} providing error context values for {@link CannotCreateReferencedItemException}s
 */
public final class CannotCreateReferenceItemExceptionContextPopulator implements ErrorContextPopulator
{
	@Override
	public void populate(@NotNull final ODataErrorContext context)
	{
		final var contextException = context.getException();
		if (getExceptionClass().isInstance(contextException))
		{
			final var exception = (CannotCreateReferencedItemException) contextException;
			context.setHttpStatus(HttpStatusCodes.BAD_REQUEST);
			context.setErrorCode("missing_nav_property");
			context.setMessage(exception.getMessage());
			context.setLocale(Locale.ENGLISH);
			context.setInnerError(exception.getIntegrationKey());
		}
	}

	@Override
	public Class<? extends Exception> getExceptionClass()
	{
		return CannotCreateReferencedItemException.class;
	}
}
