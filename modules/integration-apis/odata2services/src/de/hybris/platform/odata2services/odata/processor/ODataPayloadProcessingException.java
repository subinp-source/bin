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

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;

public class ODataPayloadProcessingException extends OdataRequestProcessingException
{
	private static final String ODATA_PROCESSING_EXCEPTION_MESSAGE = "An error occurred while processing the request."
			+ " The most likely cause of this error is the formatting of your OData request payload. Please verify the"
			+ " request payload format corresponds to the EDMX metadata for the Entity that you are attempting to create.";
	/**
	 * Constructor to create ODataPayloadProcessingException
	 *
	 * @param e exception that was thrown
	 */
	public ODataPayloadProcessingException(final Throwable e)
	{
		super(ODATA_PROCESSING_EXCEPTION_MESSAGE, HttpStatusCodes.BAD_REQUEST, e);
	}
}
