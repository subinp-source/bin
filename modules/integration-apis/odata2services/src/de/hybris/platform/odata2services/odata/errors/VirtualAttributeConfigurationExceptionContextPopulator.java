/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.errors;

import de.hybris.platform.integrationservices.virtualattributes.VirtualAttributeConfigurationException;

import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.processor.ODataErrorContext;

/**
 * An {@link ErrorContextPopulator} providing error values for {@link VirtualAttributeConfigurationException}s
 */
public class VirtualAttributeConfigurationExceptionContextPopulator implements ErrorContextPopulator
{
	private static final String MESSAGE = "There was an error reading the attribute [%s] for EntityType [%s].";

	@Override
	public void populate(@NotNull final ODataErrorContext context)
	{
		final var contextException = context.getException();
		if (contextException instanceof VirtualAttributeConfigurationException)
		{
			final var ex = (VirtualAttributeConfigurationException) contextException;
			context.setHttpStatus(HttpStatusCodes.BAD_REQUEST);
			context.setMessage(formatMessage(ex));
			context.setErrorCode("misconfigured_attribute");
			context.setLocale(Locale.ENGLISH);
		}
	}

	@Override
	public Class<? extends Exception> getExceptionClass()
	{
		return VirtualAttributeConfigurationException.class;
	}

	private String formatMessage(final VirtualAttributeConfigurationException ex)
	{
		return String.format(MESSAGE, ex.getAttributeName(), ex.getIntegrationItemCode());
	}
}
