/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.errors;

import de.hybris.platform.inboundservices.persistence.validation.InstanceCreationOfAbstractTypeException;

import java.util.Locale;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.processor.ODataErrorContext;

/**
 * An {@link ErrorContextPopulator} that provides error values for {@link InstanceCreationOfAbstractTypeException}s.
 */
public class InstanceCreationOfAbstractTypeExceptionContextPopulator implements ErrorContextPopulator
{
	private static final String ERROR_CODE = "invalid_type";

	@Override
	public void populate(final ODataErrorContext context)
	{
		if (context.getException() instanceof InstanceCreationOfAbstractTypeException)
		{
			final var exception = (InstanceCreationOfAbstractTypeException) context.getException();
			context.setHttpStatus(HttpStatusCodes.BAD_REQUEST);
			context.setErrorCode(ERROR_CODE);
			context.setLocale(Locale.ENGLISH);
			context.setMessage(exception.getMessage());
		}
	}

	@Override
	public Class<InstanceCreationOfAbstractTypeException> getExceptionClass()
	{
		return InstanceCreationOfAbstractTypeException.class;
	}
}
