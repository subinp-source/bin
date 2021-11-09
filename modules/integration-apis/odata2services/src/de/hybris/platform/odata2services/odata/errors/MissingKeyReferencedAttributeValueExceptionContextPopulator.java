/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.errors;

import de.hybris.platform.integrationservices.exception.MissingKeyReferencedAttributeValueException;

import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.processor.ODataErrorContext;

/**
 * An {@link ErrorContextPopulator} providing error values for {@link MissingKeyReferencedAttributeValueException}s
 */
public class MissingKeyReferencedAttributeValueExceptionContextPopulator implements ErrorContextPopulator
{
	private static final String MESSAGE = "Key NavigationProperty [%s] is required for EntityType [%s].";

	@Override
	public void populate(@NotNull final ODataErrorContext context)
	{
		final var contextException = context.getException();
		if (contextException instanceof MissingKeyReferencedAttributeValueException)
		{
			final var exception = (MissingKeyReferencedAttributeValueException) contextException;
			context.setHttpStatus(HttpStatusCodes.BAD_REQUEST);
			context.setErrorCode("missing_key");
			context.setMessage(message(exception));
			context.setLocale(Locale.ENGLISH);
		}
	}

	private static String message(final MissingKeyReferencedAttributeValueException e)
	{
		return String.format(MESSAGE, e.getAttributeName(), e.getIntegrationItemCode());
	}

	@Override
	public Class<MissingKeyReferencedAttributeValueException> getExceptionClass()
	{
		return MissingKeyReferencedAttributeValueException.class;
	}
}
