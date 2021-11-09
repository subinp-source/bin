/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.errors;

import de.hybris.platform.integrationservices.util.Log;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.processor.ODataErrorCallback;
import org.apache.olingo.odata2.api.processor.ODataErrorContext;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.slf4j.Logger;

/**
 * ODataErrorCallback implementation that populates the Context with
 * Status, ErrorCode and Message for custom OData runtime exceptions.
 */
public class CustomODataExceptionAwareErrorCallback implements ODataErrorCallback, ErrorContextPopulator
{
	private static final Logger LOG = Log.getLogger(CustomODataExceptionAwareErrorCallback.class);
	private static final ExceptionContextPopulator DEFAULT_ERROR_CONTEXT_PROVIDER = new ExceptionContextPopulator();

	private final Map<Class<?>, ErrorContextPopulator> errorContextPopulators;

	public CustomODataExceptionAwareErrorCallback()
	{
		errorContextPopulators = new HashMap<>();
	}

	@Override
	public ODataResponse handleError(final ODataErrorContext context)
	{
		populate(context);
		return EntityProvider.writeErrorDocument(context);
	}

	/**
	 * {@inheritDoc}
	 * <p>It works as a composite {@link ErrorContextPopulator}, which delegates to other registered populators</p>
	 * @param context a context to update with the exception specific values.
	 * @see #setErrorContextPopulators(Collection)
	 */
	@Override
	public void populate(final ODataErrorContext context)
	{
		final Exception contextException = context.getException();
		final String message = nullSafeMessage(contextException);
		LOG.error("Handling exception {}", message);
		LOG.debug("Exception details", contextException);

		final Class<? extends Exception> exClass = contextException != null
				? contextException.getClass()
				: getExceptionClass();
		findMatchingProvider(exClass)
				.orElse(DEFAULT_ERROR_CONTEXT_PROVIDER)
				.populate(context);
	}

	private static String nullSafeMessage(final Exception e)
	{
		return e != null
				? (e.getClass() + ": " + e.getMessage())
				: null;
	}

	private Optional<ErrorContextPopulator> findMatchingProvider(final Class<?> exType)
	{
		if (getExceptionClass().isAssignableFrom(exType))
		{
			final ErrorContextPopulator populator = errorContextPopulators.get(exType);
			return populator != null
					? Optional.of(populator)
					: findMatchingProvider(exType.getSuperclass());
		}
		return Optional.empty();
	}

	@Override
	public Class<? extends Exception> getExceptionClass()
	{
		return Exception.class;
	}

	/**
	 * Sets populators to be used for error conversion. An exception handling can be customized by registering a specific for that
	 * exception populator, which presents that exception in the error response.
	 * @param populators populators handling different kind of exceptions. If there several populators for the same
	 * {@link ErrorContextPopulator#getExceptionClass()}, then the last one (from the iteration stand point) will be used.
	 * All previous populators will be ignored.
	 */
	public void setErrorContextPopulators(final Collection<ErrorContextPopulator> populators)
	{
		errorContextPopulators.clear();
		Optional.ofNullable(populators)
				.orElseGet(Collections::emptyList)
				.forEach(this::addErrorContextPopulator);
	}

	/**
	 * Adds a populator to the populators already existing in this callback.
	 * @param populator a populator to add to existing populators.
	 */
	public void addErrorContextPopulator(final ErrorContextPopulator populator)
	{
		errorContextPopulators.put(populator.getExceptionClass(), populator);
	}
}

