/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.errors;

import de.hybris.platform.odata2services.filter.IntegrationKeyNestedFilteringNotSupportedException;
import java.util.Locale;
import javax.validation.constraints.NotNull;
import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.processor.ODataErrorContext;

/**
 * An {@link ErrorContextPopulator} providing error values for {@link IntegrationKeyNestedFilteringNotSupportedException}s
 */
public class IntegrationKeyNestedFilteringNotSupportedExceptionPopulator implements ErrorContextPopulator
{
	private static final String ERROR_CODE = "operation_not_supported";

	@Override
	public void populate(@NotNull final ODataErrorContext context)
	{
		final var contextException = context.getException();
		if (contextException instanceof IntegrationKeyNestedFilteringNotSupportedException)
		{
			final var exception = (IntegrationKeyNestedFilteringNotSupportedException) contextException;
			context.setHttpStatus(HttpStatusCodes.BAD_REQUEST);
			context.setErrorCode(ERROR_CODE);
			context.setMessage(exception.getMessage());
			context.setLocale(Locale.ENGLISH);
		}
	}

	@Override
	public Class<IntegrationKeyNestedFilteringNotSupportedException> getExceptionClass()
	{
		return IntegrationKeyNestedFilteringNotSupportedException.class;
	}
}
