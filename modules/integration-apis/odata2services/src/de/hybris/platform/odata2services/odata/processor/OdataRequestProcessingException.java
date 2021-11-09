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
package de.hybris.platform.odata2services.odata.processor;

import de.hybris.platform.odata2services.odata.persistence.PersistenceRuntimeApplicationException;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;

public class OdataRequestProcessingException extends PersistenceRuntimeApplicationException
{
	private static final String ERROR_CODE = "odata_error";

	/**
	 * Constructor to create OdataRequestProcessingException
	 *
	 * @param message error message
	 * @param statusCode response status code
	 * @param cause exception that was thrown
	 */
	public OdataRequestProcessingException(final String message, final HttpStatusCodes statusCode, final Throwable cause)
	{
		super(message, statusCode, ERROR_CODE, cause);
	}
}
