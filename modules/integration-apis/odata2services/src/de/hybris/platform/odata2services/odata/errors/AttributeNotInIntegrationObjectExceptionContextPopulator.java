/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.errors;

import java.util.List;
import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.processor.ODataErrorContext;

/**
 * An error context populator for {@link AttributeNotInIntegrationObjectException}
 */
public class AttributeNotInIntegrationObjectExceptionContextPopulator implements ErrorContextPopulator
{

	private static final String PROPERTIES = "properties ";
	private static final String PROPERTY = "property ";

	@Override
	public void populate(@NotNull final ODataErrorContext context)
	{
		final var exception = context.getException();
		if (exception instanceof AttributeNotInIntegrationObjectException)
		{
			final AttributeNotInIntegrationObjectException persistenceException = (AttributeNotInIntegrationObjectException) exception;
			context.setHttpStatus(HttpStatusCodes.BAD_REQUEST);
			context.setErrorCode(persistenceException.getErrorCode());
			context.setMessage(deriveMessage(persistenceException.getContent()));
			context.setLocale(Locale.ENGLISH);
		}
	}

	private String deriveMessage(final List<Object> content)
	{
		return "An entity contains " + ((content.size() > 1) ? PROPERTIES : PROPERTY) + content.toString() + " that is not defined in the integration object";
	}

	@Override
	public Class<? extends Exception> getExceptionClass()
	{
		return AttributeNotInIntegrationObjectException.class;
	}
}
