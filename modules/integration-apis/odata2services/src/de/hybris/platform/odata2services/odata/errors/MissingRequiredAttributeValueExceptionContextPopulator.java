/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.errors;

import de.hybris.platform.inboundservices.persistence.populator.MissingRequiredAttributeValueException;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.processor.ODataErrorContext;

/**
 * An {@link ErrorContextPopulator} that provides context error values for any {@link MissingRequiredAttributeValueException}s.
 */
public class MissingRequiredAttributeValueExceptionContextPopulator implements ErrorContextPopulator
{
	private static final String PRIMITIVE_ERROR_CODE = "missing_property";
	private static final String REFERENCE_ERROR_CODE = "missing_nav_property";
	private static final String PRIMITIVE_ERROR_MESSAGE = "Property [%s] is required for EntityType [%s].";
	private static final String REFERENCE_ERROR_MESSAGE = "Required navigationProperty for EntityType [%s] does not exist in the System";
	@Override
	public void populate(@NotNull final ODataErrorContext context)
	{
		final var contextException = context.getException();
		if (contextException instanceof MissingRequiredAttributeValueException)
		{
			final var exception = (MissingRequiredAttributeValueException) contextException;
			context.setHttpStatus(HttpStatusCodes.BAD_REQUEST);
			context.setErrorCode(errorCode(exception));
			context.setMessage(message(exception));
			context.setInnerError(exception.getIntegrationKey());
			context.setLocale(Locale.ENGLISH);
		}
	}

	public String errorCode(final MissingRequiredAttributeValueException exception){
		if (isReference(exception.getAttributeDescriptor())){
			return REFERENCE_ERROR_CODE;
		}
		return PRIMITIVE_ERROR_CODE;
	}

	public String message(final MissingRequiredAttributeValueException exception){
		if (isReference(exception.getAttributeDescriptor())){
			return "Missing [" + exception.getAttributeName() + "]. " + String.format(REFERENCE_ERROR_MESSAGE, exception.getIntegrationItemCode());
		}
		return String.format(PRIMITIVE_ERROR_MESSAGE, exception.getAttributeName(), exception.getIntegrationItemCode());
	}

	private boolean isReference(final TypeAttributeDescriptor descriptor)
	{
		return descriptor.isCollection() || descriptor.isMap() || !descriptor.isPrimitive();
	}

	@Override
	public Class<? extends Exception> getExceptionClass()
	{
		return MissingRequiredAttributeValueException.class;
	}
}
