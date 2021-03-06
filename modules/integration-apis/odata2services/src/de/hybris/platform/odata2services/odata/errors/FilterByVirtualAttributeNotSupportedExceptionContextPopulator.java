/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.errors;

import de.hybris.platform.integrationservices.search.FilterByVirtualAttributeNotSupportedException;
import de.hybris.platform.integrationservices.search.OrderByVirtualAttributeNotSupportedException;

import java.util.Locale;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.processor.ODataErrorContext;

/**
 * An {@link ErrorContextPopulator} for handling {@link OrderByVirtualAttributeNotSupportedException}s.
 */
public class FilterByVirtualAttributeNotSupportedExceptionContextPopulator implements ErrorContextPopulator
{
	@Override
	public void populate(final ODataErrorContext context)
	{
		final var ex = context.getException();
		if (ex instanceof FilterByVirtualAttributeNotSupportedException)
		{
			context.setHttpStatus(HttpStatusCodes.BAD_REQUEST);
			context.setMessage(ex.getMessage());
			context.setLocale(Locale.ENGLISH);
			context.setErrorCode("filter_by_virtual_attribute_not_supported");
		}
	}

	@Override
	public Class<? extends Exception> getExceptionClass()
	{
		return FilterByVirtualAttributeNotSupportedException.class;
	}
}