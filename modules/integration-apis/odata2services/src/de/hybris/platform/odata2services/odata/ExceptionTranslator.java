/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved
 */
package de.hybris.platform.odata2services.odata;

import de.hybris.platform.integrationservices.exception.IntegrationAttributeException;
import de.hybris.platform.odata2services.odata.errors.ErrorContextPopulator;

import org.apache.olingo.odata2.api.exception.ODataRuntimeApplicationException;
import org.apache.olingo.odata2.api.processor.ODataErrorContext;

/**
 * @deprecated Use {@link ErrorContextPopulator} instead.
 */
@Deprecated(since = "1905.07-CEP", forRemoval = true)
public interface ExceptionTranslator extends ErrorContextPopulator
{
	ODataRuntimeApplicationException translate(final IntegrationAttributeException e);

	@Override
	default void populate(final ODataErrorContext context)
	{
		final var contextException = context.getException();
		if (contextException instanceof IntegrationAttributeException)
		{
			final ODataRuntimeApplicationException ex = translate((IntegrationAttributeException) contextException);
			context.setHttpStatus(ex.getHttpStatus());
			context.setMessage(ex.getMessage());
			context.setLocale(ex.getLocale());
			context.setErrorCode("misconfigured_attribute");
		}
	}

	@Override
	default Class<IntegrationAttributeException> getExceptionClass()
	{
		return IntegrationAttributeException.class;
	}
}
