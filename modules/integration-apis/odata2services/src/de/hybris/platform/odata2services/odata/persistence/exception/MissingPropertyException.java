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
 * Exception to thrown when a required property does not already exist.
 *
 * Will result in HttpStatus 400
 */
public class MissingPropertyException extends MissingDataException
{
	/**
	 * Constructor to create MissingPropertyException
	 *
	 * @param entityType entity type
	 * @param propertyName the name of the property
	 */
	public MissingPropertyException(final String entityType, final String propertyName)
	{
		super(String.format("Property [%s] is required for EntityType [%s].", propertyName, entityType), "missing_property", propertyName, entityType);
	}
}
