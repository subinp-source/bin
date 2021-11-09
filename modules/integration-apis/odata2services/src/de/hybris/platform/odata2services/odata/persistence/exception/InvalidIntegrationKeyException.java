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

import de.hybris.platform.odata2services.odata.persistence.lookup.InvalidLookupDataException;

import org.apache.olingo.odata2.api.edm.EdmEntityType;

/**
 * An exception that occurs when the integrationKey value does not correspond with the integrationKey metadata definition.
 */
public class InvalidIntegrationKeyException extends InvalidLookupDataException
{
	private static final String ERROR_CODE = "invalid_key";
	private static final String BASE_MESSAGE_WITH_TYPE = "Please consult the IntegrationKey definition of [%s] for configuration details.";
	private static final String INVALID_KEY_WITH_VALUE = "The integration key [%s] is invalid. " + BASE_MESSAGE_WITH_TYPE;
	private static final String INVALID_KEY = "The integration key is invalid. " + BASE_MESSAGE_WITH_TYPE;

	private final String entityType;

	/**
	 * Constructor to create InvalidIntegrationKeyException
	 *
	 * @param integrationKey key value
	 * @param entityType {@link EdmEntityType} name
	 */
	public InvalidIntegrationKeyException(final String integrationKey, final String entityType)
	{
		super(String.format(INVALID_KEY_WITH_VALUE, integrationKey, entityType), ERROR_CODE);
		this.entityType = entityType;
	}

	/**
	 * Constructor to create InvalidIntegrationKeyException
	 *
	 * @param entityType {@link EdmEntityType} name
	 */
	public InvalidIntegrationKeyException(final String entityType)
	{
		super(String.format(INVALID_KEY, entityType), ERROR_CODE);
		this.entityType = entityType;
	}

	public String getEntityType()
	{
		return entityType;
	}
}
