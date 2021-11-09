/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved
 */

package de.hybris.platform.odata2services.odata.persistence.exception;

import de.hybris.platform.odata2services.odata.InvalidDataException;

/**
 * Exception thrown when the entityType has no valid key defined
 *
 * Will result in HttpStatus 400
 */
public class MissingKeyException extends InvalidDataException
{
	/**
	 * Constructor to create MissingKeyException
	 *
	 * @param entityType entity type
	 */
	public MissingKeyException(final String entityType)
	{
		super(String.format("There is no valid integration key defined for the current entityType [%s].", entityType), "invalid_key_definition", entityType);
	}
}
