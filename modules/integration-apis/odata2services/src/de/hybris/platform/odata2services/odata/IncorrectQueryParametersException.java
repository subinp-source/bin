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

/**
 * Thrown when the query parameters in the OData Request are not valid and they cannot be parsed into
 * an entity type.
 */
public class IncorrectQueryParametersException extends OData2ServicesException
{
	private static final String ERROR_CODE = "invalid_metadata_parameter";

	/**
	 *  Constructor to create IncorrectQueryParametersException
	 */
	public IncorrectQueryParametersException()
	{
		super("The query parameters cannot be parsed to an entity type.", HttpStatusCodes.BAD_REQUEST, ERROR_CODE);
	}
}
