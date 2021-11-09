/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved
 */
package de.hybris.platform.odata2services.odata.impl;

import de.hybris.platform.integrationservices.exception.IntegrationAttributeException;
import de.hybris.platform.odata2services.odata.ExceptionTranslator;
import de.hybris.platform.odata2services.odata.errors.IntegrationAttributeExceptionContextPopulator;

import java.util.Locale;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.exception.ODataRuntimeApplicationException;

/**
 * @deprecated use {@link IntegrationAttributeExceptionContextPopulator} instead
 */
@Deprecated(since = "1905.07-CEP", forRemoval = true)
public class DefaultExceptionTranslator implements ExceptionTranslator
{
	private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

	@Override
	public ODataRuntimeApplicationException translate(final IntegrationAttributeException e)
	{
		return new ODataRuntimeApplicationException(e.getMessage(), DEFAULT_LOCALE, HttpStatusCodes.BAD_REQUEST);
	}
}
