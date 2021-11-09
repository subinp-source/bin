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

import de.hybris.platform.odata2services.odata.processor.OdataRequestProcessingException;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;

public class OdataRequestDataProcessingException extends OdataRequestProcessingException
{
	/**
	 * Constructor to create OdataRequestDataProcessingException
	 * 
	 * @param cause exception that was thrown
	 */
	public OdataRequestDataProcessingException(final Throwable cause)
	{
		super("An error was encountered while extracting data from the request.", HttpStatusCodes.INTERNAL_SERVER_ERROR, cause);
	}
}
