/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.odata2services.odata.persistence;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.odata2services.odata.OData2ServicesException;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;

/**
 * Indicates an attempt to use an invalid or unsupported ISO code with the localized data.
 */
public class LanguageNotSupportedException extends OData2ServicesException
{
	private static final HttpStatusCodes STATUS_CODE = HttpStatusCodes.BAD_REQUEST;
	private static final String ERROR_CODE = "invalid_language";
	private final String language;

	/**
	 * Instantiates this exception without a root cause.
	 * @param isoCode an invalid ISO code or an ISO code that is not supported by the Integration API
	 */
	public LanguageNotSupportedException(final String isoCode)
	{
		this(isoCode, null);
	}

	/**
	 * Constructor to create LanguageNotSupportedException
	 *
	 * @param language language that does not correspond to an existing {@link LanguageModel}
	 * @param e exception that was thrown
	 */
	public LanguageNotSupportedException(final String language, final Exception e)
	{
		super(String.format("The language provided [%s] is not available in the system.", language), STATUS_CODE, ERROR_CODE, e);
		this.language = language;
	}

	public String getLanguage()
	{
		return language;
	}
}
