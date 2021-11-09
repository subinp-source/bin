/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.errors;

import de.hybris.platform.integrationservices.security.TypeAccessPermissionException;

import java.util.Locale;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.processor.ODataErrorContext;


/**
 * An {@link ErrorContextPopulator} providing error values for {@link TypeAccessPermissionException}s
 */
public class TypeAccessPermissionExceptionErrorPopulator implements ErrorContextPopulator
{
	@Override
	public void populate(final ODataErrorContext context)
	{
		final var contextException = context.getException();
		if (contextException instanceof TypeAccessPermissionException)
		{
			final var exception = (TypeAccessPermissionException) contextException;
			context.setHttpStatus(HttpStatusCodes.FORBIDDEN);
			context.setErrorCode("forbidden");
			context.setLocale(Locale.ENGLISH);
			context.setMessage(exception.getMessage());
		}
	}

	@Override
	public Class<? extends Exception> getExceptionClass()
	{
		return TypeAccessPermissionException.class;
	}
}
