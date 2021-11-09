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
 * Thrown when an invalid OData Schema metadata has been defined for Integration objects. This is a general exception for any
 * error, the cause will have a more specific message as to what exactly went wrong when the schema was attempted to be parsed.
 */
public class InvalidODataSchemaException extends OData2ServicesException
{
	private static final String MESSAGE = "The EDMX schema could not be generated. Please make sure that your Integration Object is defined correctly.";

	/**
	 * Constructor to create InvalidODataSchemaException
	 *
	 * @param cause exception that was thrown
	 */
	public InvalidODataSchemaException(final Throwable cause)
	{
		super(MESSAGE, HttpStatusCodes.INTERNAL_SERVER_ERROR, "schema_generation_error", cause);
	}
}
