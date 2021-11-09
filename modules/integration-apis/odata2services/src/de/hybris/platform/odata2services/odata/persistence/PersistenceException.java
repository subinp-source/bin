/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.persistence;

import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import org.apache.commons.lang3.StringUtils;
import org.apache.olingo.odata2.api.commons.HttpStatusCodes;

/**
 * Indicates a problem with persisting a request paylaod.
 */
public class PersistenceException extends PersistenceRuntimeApplicationException
{
	private static final HttpStatusCodes DEFAULT_STATUS_CODE = HttpStatusCodes.INTERNAL_SERVER_ERROR;
	private static final String DEFAULT_ERROR_CODE = "internal_error";
	private static final String INVALID_ATTRIBUTE_VALUE_ERROR_CODE = "invalid_attribute_value";

	/**
	 * Constructor to create PersistenceException
	 *
	 * @param e   exception that was thrown
	 * @param ctx object that holds values for creating or updating an item
	 */
	public PersistenceException(final Throwable e, final PersistenceContext ctx)
	{
		super(generateMessage(e, ctx), getStatusCode(e), getErrorCode(e), e, ctx.getIntegrationItem().getIntegrationKey());
	}

	private static String generateMessage(final Throwable t, final PersistenceContext ctx)
	{
		return String.format("An error occurred while attempting to save entries for entityType: %s%s",
				ctx.getIntegrationItem().getItemType().getItemCode(), getAdditionalMessage(t));
	}

	private static String getAdditionalMessage(final Throwable t)
	{
		final String prefix = ", with error message ";
		if (isInterceptorException(t) || isInvalidParameterException(t))
		{
			return prefix + extractExceptionCauseDetail(t);
		}
		else if (t instanceof SystemException)
		{
			return prefix + t.getMessage();
		}
		return "";
	}

	private static String extractExceptionCauseDetail(final Throwable t)
	{
		return messageContainsPackageAndClassName(t) ? extractDetailMessageWithoutClassName(t) : t.getCause().getMessage();
	}

	private static String extractDetailMessageWithoutClassName(final Throwable t)
	{
		return StringUtils.substringAfter(t.getCause().getMessage(), "]:");
	}

	private static boolean messageContainsPackageAndClassName(final Throwable t)
	{
		return t.getCause().getMessage().contains("]:");
	}

	private static String getErrorCode(final Throwable t)
	{
		return (isInterceptorException(t) || isInvalidParameterException(t)) ?
				INVALID_ATTRIBUTE_VALUE_ERROR_CODE :
				DEFAULT_ERROR_CODE;
	}

	private static HttpStatusCodes getStatusCode(final Throwable t)
	{
		return (isInterceptorException(t)  || isInvalidParameterException(t)) ?
				HttpStatusCodes.BAD_REQUEST :
				DEFAULT_STATUS_CODE;
	}

	private static boolean isInterceptorException(final Throwable t)
	{
		return t.getCause() instanceof InterceptorException;
	}

	private static boolean isInvalidParameterException(final Throwable t)
	{
		return t.getCause() instanceof JaloInvalidParameterException;
	}
}
