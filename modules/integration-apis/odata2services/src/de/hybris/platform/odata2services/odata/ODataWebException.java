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
package de.hybris.platform.odata2services.odata;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.exception.ODataException;

/**
 * This exception wraps a thrown {@link ODataException}
 *
 * Will result in HttpStatus 500
 */
public class ODataWebException extends OData2ServicesException
{
	/**
	 * Constructor to create ODataWebException
	 *
	 * @param message message indicating the cause of this exception
	 * @param cause {@link ODataException} that was thrown
	 */
	public ODataWebException(final String message, final ODataException cause)
	{
		super(message, HttpStatusCodes.INTERNAL_SERVER_ERROR, "odata2_service_error", cause);
	}
}
