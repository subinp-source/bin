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

import de.hybris.platform.odata2services.odata.InvalidDataException;

import org.apache.olingo.odata2.api.edm.EdmEntityType;

public class RetrievalErrorRuntimeException extends InvalidDataException
{
	private static final String RETRIEVAL_ITEM_RUNTIME_EXCEPTION_MESSAGE =
			"There was a problem with the retrieval of the requested [%s].";

	/**
	 * Constructor to create RetrievalErrorRuntimeException
	 *
	 * @param entityTypeName {@link EdmEntityType} name
	 * @param throwable exception that was thrown
	 */
	public RetrievalErrorRuntimeException(final String entityTypeName, final Throwable throwable)
	{
		super(String.format(RETRIEVAL_ITEM_RUNTIME_EXCEPTION_MESSAGE, entityTypeName), "runtime_error", throwable, entityTypeName);
	}
}
