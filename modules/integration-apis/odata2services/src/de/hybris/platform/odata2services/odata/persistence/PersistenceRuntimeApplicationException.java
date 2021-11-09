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

import de.hybris.platform.integrationservices.jalo.IntegrationObjectItem;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.exception.ODataRuntimeApplicationException;


public class PersistenceRuntimeApplicationException extends ODataRuntimeApplicationException
{
	private final String integrationKey;

	/**
	 * Constructor to create PersistenceRuntimeApplicationException
	 *
	 * @param message error message
	 * @param statusCode error status code
	 * @param errorCode error code
	 * @param cause exception that was thrown
	 */
	public PersistenceRuntimeApplicationException(final String message, final HttpStatusCodes statusCode, final String errorCode, final Throwable cause)
	{
		super(message, Locale.ENGLISH, statusCode, errorCode, cause);
		this.integrationKey = StringUtils.EMPTY;
	}

	/**
	 * Constructor to create PersistenceRuntimeApplicationException
	 *
	 * @param message error message
	 * @param statusCode error status code
	 * @param errorCode error code
	 * @param cause exception that was thrown
	 * @param integrationKey integration key for the {@link IntegrationObjectItem} that has failed to persist
	 */
	public PersistenceRuntimeApplicationException(final String message, final HttpStatusCodes statusCode, final String errorCode, final Throwable cause, final String integrationKey)
	{
		super(message, Locale.ENGLISH, statusCode, errorCode, cause);
		this.integrationKey = integrationKey;
	}

	/**
	 * Constructor to create PersistenceRuntimeApplicationException
	 * 
	 * @param message error message
	 * @param statusCode error status code
	 * @param errorCode error code
	 * @param integrationKey integration key for the {@link IntegrationObjectItem} that has failed to persist
	 */
	public PersistenceRuntimeApplicationException(final String message, final HttpStatusCodes statusCode, final String errorCode, final String integrationKey)
	{
		super(message, Locale.ENGLISH, statusCode, errorCode);
		this.integrationKey = integrationKey;
	}
	
	public String getIntegrationKey()
	{
		return integrationKey;
	}
}
