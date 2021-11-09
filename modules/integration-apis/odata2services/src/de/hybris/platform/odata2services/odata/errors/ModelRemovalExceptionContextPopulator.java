/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.errors;

import de.hybris.platform.inboundservices.persistence.populator.InboundChannelConfigurationDeletionException;
import de.hybris.platform.inboundservices.persistence.populator.IntegrationObjectDeletionException;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;

import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.processor.ODataErrorContext;

/**
 * An {@link ErrorContextPopulator} for handling {@link ModelRemovalExceptionContextPopulator}s.
 */
public class ModelRemovalExceptionContextPopulator implements ErrorContextPopulator
{
	private static final String ERROR_CODE = "deletion_failure";

	@Override
	public void populate(@NotNull final ODataErrorContext context)
	{
		if (context.getException() instanceof ModelRemovalException)
		{
			final var ex = (ModelRemovalException) context.getException();
			if (ex.getCause() instanceof InboundChannelConfigurationDeletionException)
			{
				context.setMessage(((InboundChannelConfigurationDeletionException) ex.getCause()).getErrorMessage());
			}
			else if (ex.getCause() instanceof IntegrationObjectDeletionException)
			{
				context.setMessage(((IntegrationObjectDeletionException) ex.getCause()).getErrorMessage());
			}
			else
			{
				context.setMessage(ex.getMessage());
			}
			context.setErrorCode(ERROR_CODE);
			context.setHttpStatus(HttpStatusCodes.BAD_REQUEST);
			context.setLocale(Locale.ENGLISH);
		}
	}

	@Override
	public Class<? extends Exception> getExceptionClass()
	{
		return ModelRemovalException.class;
	}

}

