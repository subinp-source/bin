/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.errors;

import de.hybris.platform.integrationservices.virtualattributes.VirtualAttributeExecutionException;

import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.processor.ODataErrorContext;

/**
 *  An {@link ErrorContextPopulator} providing error values for {@link VirtualAttributeExecutionException}s
 */
public class VirtualAttributeExecutionExceptionContextPopulator implements ErrorContextPopulator
{
	private static final String MESSAGE = "There was an unexpected error encountered during the retrieval of the [%s] property for EntityType [%s].";

	@Override
	public void populate(@NotNull final ODataErrorContext context)
	{
		final var contextException = context.getException();
		if (contextException instanceof VirtualAttributeExecutionException)
		{
			final var ex = (VirtualAttributeExecutionException) contextException;
			context.setHttpStatus(HttpStatusCodes.BAD_REQUEST);
			context.setMessage(formatMessage(ex));
			context.setErrorCode("runtime_error");
			context.setLocale(Locale.ENGLISH);
		}
	}

	@Override
	public Class<? extends Exception> getExceptionClass()
	{
		return VirtualAttributeExecutionException.class;
	}

	private String formatMessage(final VirtualAttributeExecutionException ex)
	{
		return String.format(MESSAGE, ex.getAttributeName(), ex.getIntegrationItemCode());
	}
}
