/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.errors;

import de.hybris.platform.inboundservices.persistence.populator.UnmodifiableAttributeException;

import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.processor.ODataErrorContext;

/**
 * An {@link ErrorContextPopulator} providing error values for {@link UnmodifiableAttributeException}s
 */
public final class UnmodifiableAttributeExceptionContextPopulator implements ErrorContextPopulator
{
	@Override
	public void populate(@NotNull final ODataErrorContext context)
	{
		final var contextException = context.getException();
		if (contextException instanceof UnmodifiableAttributeException)
		{
			final var exception = (UnmodifiableAttributeException) contextException;
			context.setHttpStatus(HttpStatusCodes.BAD_REQUEST);
			context.setErrorCode("unmodifiable_attribute");
			context.setMessage(exception.getMessage());
			context.setInnerError(exception.getIntegrationKey());
			context.setLocale(Locale.ENGLISH);
		}
	}

	@Override
	public Class<UnmodifiableAttributeException> getExceptionClass()
	{
		return UnmodifiableAttributeException.class;
	}
}
