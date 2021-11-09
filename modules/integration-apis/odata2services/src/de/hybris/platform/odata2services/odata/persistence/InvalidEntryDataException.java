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

import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.odata2services.odata.OData2ServicesException;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;

/**
 * This exception wraps a thrown {@link OData2ServicesException}
 */
public class InvalidEntryDataException extends PersistenceRuntimeApplicationException
{
	private static final HttpStatusCodes STATUS_CODE = HttpStatusCodes.BAD_REQUEST;

	/**
	 * Constructor to create InvalidEntryDataException to populate the integrationKey in the error response.
	 *
	 * @param e exception that was thrown
	 * @param ctx object that holds values for creating or updating an item
	 */
	public InvalidEntryDataException(final OData2ServicesException e, final PersistenceContext ctx)
	{
		super(e.getMessage(), STATUS_CODE, e.getCode(), ctx.getIntegrationItem().getIntegrationKey());
	}
}
