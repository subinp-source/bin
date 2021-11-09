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

import org.apache.olingo.odata2.api.edm.EdmEntityType;

/**
 * Exception to thrown when a key navigationProperty element for the entityType is not supplied in the request.
 *
 * Will result in HttpStatus 400
 */
public class MissingKeyNavigationPropertyException extends MissingDataException
{
	private static final String MESSAGE = "Key NavigationProperty [%s] is required for EntityType [%s].";

	/**
	 * Constructor to create MissingKeyNavigationPropertyException
	 *
	 * @param entityType {@link EdmEntityType} name
	 * @param propertyName the name of the property
	 */
	public MissingKeyNavigationPropertyException(final String entityType, final String propertyName)
	{
		super(String.format(MESSAGE, propertyName, entityType), "missing_key", propertyName, entityType);
	}
}
