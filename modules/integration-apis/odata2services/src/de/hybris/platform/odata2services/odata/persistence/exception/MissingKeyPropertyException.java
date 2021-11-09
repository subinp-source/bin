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

package de.hybris.platform.odata2services.odata.persistence.exception;

import de.hybris.platform.odata2services.odata.persistence.MissingDataException;

/**
 * Exception to thrown when a key property element for the entityType is not supplied in the request.
 *
 * Will result in HttpStatus 400
 */
public class MissingKeyPropertyException extends MissingDataException
{
	private static final String MESSAGE = "Key [%s] is required for EntityType [%s]";

	/**
	 * Constructor to create MissingKeyPropertyException
	 *
	 * @param entityType entity type
	 * @param propertyName the name of the property
	 */
	public MissingKeyPropertyException(final String entityType, final String propertyName)
	{
		super(String.format(MESSAGE, propertyName, entityType), "missing_key", propertyName, entityType);
	}
}
