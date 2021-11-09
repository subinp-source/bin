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

import de.hybris.platform.odata2services.odata.InvalidDataException;

import org.apache.olingo.odata2.api.edm.EdmEntityType;

public class MissingLanguageException extends InvalidDataException
{
	private static final String ERROR_CODE = "missing_language";
	private static final String BASE_MESSAGE = "'language' is a required attribute for localized types. Please resubmit and make sure that each %s contains the 'language' attribute.";
	private static final String MISSING_LANGUAGE_MESSAGE = "%s " + BASE_MESSAGE;

	/**
	 * Constructor to create MissingLanguageException
	 *
	 * @param localizedEntityTypeName localized {@link EdmEntityType} name
	 */
	public MissingLanguageException(final String localizedEntityTypeName)
	{
		super(message(localizedEntityTypeName), ERROR_CODE, localizedEntityTypeName);
	}

	/**
	 * Constructor to create MissingLanguageException
	 */
	public MissingLanguageException()
	{
		super(String.format(BASE_MESSAGE, "Localized___... type"), ERROR_CODE, "Localized___... type");
	}

	private static String message(final String typeName)
	{
		return String.format(MISSING_LANGUAGE_MESSAGE, typeName, typeName);
	}
}
