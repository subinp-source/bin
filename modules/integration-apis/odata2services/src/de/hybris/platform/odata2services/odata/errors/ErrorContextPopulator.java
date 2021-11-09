/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.errors;

import javax.validation.constraints.NotNull;

import org.apache.olingo.odata2.api.processor.ODataErrorContext;

/**
 * A provider of the {@link org.apache.olingo.odata2.api.processor.ODataErrorContext} values for a specific type of exception.
 */
public interface ErrorContextPopulator
{
	/**
	 * Populates context with values specific for the exception in the context, i.e. {@link ODataErrorContext#getException()}.
	 * At a minimum, the implementations should set HTTP status code, error code and message, etc in the {@code ODataErrorContext}
	 * depending on the exception.
	 * @param context a context to update with the exception specific values.
	 */
	void populate(@NotNull ODataErrorContext context);

	/**
	 * Specifies what exception class this {@code ErrorContextPopulator} can provide values for.
	 * @return the exception class
	 */
	Class<? extends Exception> getExceptionClass();
}
