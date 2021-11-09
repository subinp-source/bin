/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.errors;

import de.hybris.platform.inboundservices.persistence.populator.MissingRequiredAttributeValueException;
import de.hybris.platform.integrationservices.item.IntegrationItem;
import de.hybris.platform.integrationservices.search.validation.MissingRequiredKeyAttributeValueException;

import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.processor.ODataErrorContext;

/**
 * An {@link ErrorContextPopulator} that provides context error values for any {@link MissingRequiredAttributeValueException}s.
 */
public class MissingRequiredKeyAttributeValueExceptionContextPopulator implements ErrorContextPopulator
{
	private static final String MESSAGE_TEMPLATE = "Key %s [%s] is required for EntityType [%s].";

	@Override
	public void populate(@NotNull final ODataErrorContext context)
	{
		final var contextException = context.getException();
		if (contextException instanceof MissingRequiredKeyAttributeValueException)
		{
			final var exception = (MissingRequiredKeyAttributeValueException) contextException;
			context.setHttpStatus(HttpStatusCodes.BAD_REQUEST);
			context.setErrorCode("missing_key");
			context.setMessage(message(exception));
			context.setInnerError(getIntegrationKey(exception));
			context.setLocale(Locale.ENGLISH);
		}
	}

	private static String message(final MissingRequiredKeyAttributeValueException e)
	{
		final var attribute = e.getViolatedAttribute();
		final String property = attribute.getTypeDescriptor().isPrimitive() ? "Property" : "NavigationProperty";
		return String.format(MESSAGE_TEMPLATE, property, attribute.getAttributeName(), attribute.getTypeDescriptor().getItemCode());
	}

	private static String getIntegrationKey(final MissingRequiredKeyAttributeValueException e)
	{
		return e.getRejectedRequest().getRequestedItem()
				.map(IntegrationItem::getIntegrationKey)
				.orElse(null);
	}

	@Override
	public Class<? extends Exception> getExceptionClass()
	{
		return MissingRequiredKeyAttributeValueException.class;
	}
}
