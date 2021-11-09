/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.errors;

import de.hybris.platform.integrationservices.exception.TypeAttributeDescriptorNotFoundException;

import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.processor.ODataErrorContext;

/**
 * An error context populator for {@link TypeAttributeDescriptorNotFoundException}
 */
public class TypeAttributeDescriptorNotFoundExceptionContextPopulator implements ErrorContextPopulator
{
	@Override
	public void populate(@NotNull final ODataErrorContext context)
	{
		final var contextException = context.getException();
		if (contextException instanceof TypeAttributeDescriptorNotFoundException)
		{
			final var exception = (TypeAttributeDescriptorNotFoundException) contextException;
			context.setHttpStatus(HttpStatusCodes.BAD_REQUEST);
			context.setErrorCode("missing_attribute");
			context.setMessage(exception.getMessage());
			context.setLocale(Locale.ENGLISH);
		}
	}

	@Override
	public Class<? extends Exception> getExceptionClass()
	{
		return TypeAttributeDescriptorNotFoundException.class;
	}
} 
