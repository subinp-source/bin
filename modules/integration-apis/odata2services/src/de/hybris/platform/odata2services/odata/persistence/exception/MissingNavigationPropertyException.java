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
 * Exception to thrown when a required navigationProperty does not already exist.
 *
 * Will result in HttpStatus 400
 */
public class MissingNavigationPropertyException extends MissingDataException
{
	private static final String ERROR_CODE = "missing_nav_property";
	private static final String MISSING_ENTITY_TYPE_MESSAGE = "Required navigationProperty for EntityType [%s] does not exist in the System";

	/**
	 * Constructor to create MissingNavigationPropertyException
	 *
	 * @param entityType entity type
	 * @param propertyName the name of the property
	 */
	public MissingNavigationPropertyException(final String entityType, final String propertyName)
	{
		super("Missing [" + propertyName + "]. " + String.format(MISSING_ENTITY_TYPE_MESSAGE, entityType), ERROR_CODE, propertyName, entityType);
	}

	/**
	 * Constructor to create MissingNavigationPropertyException
	 *
	 * @param entityType entity type
	 */
	public MissingNavigationPropertyException(final String entityType)
	{
		super(String.format(MISSING_ENTITY_TYPE_MESSAGE, entityType), ERROR_CODE, "", entityType);
	}
}
