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

import de.hybris.platform.odata2services.odata.OData2ServicesException;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;

public class BatchLimitExceededException extends OData2ServicesException
{
	private static final String BATCH_LIMIT_EXCEPTION_MESSAGE = "The number of integration objects sent in the " +
			"request has exceeded the 'odata2services.batch.limit' setting currently set to %s";

	/**
	 * Constructor to create BatchLimitExceededException
	 *
	 * @param limit the int value representing the maximum number of
	 * integration objects that can be sent in a single $batch. This
	 * is a configurable property that is set with the
	 * 'odata2services.batch.limit' property
	 */
	public BatchLimitExceededException(final int limit)
	{
		super(String.format(BATCH_LIMIT_EXCEPTION_MESSAGE, limit), HttpStatusCodes.BAD_REQUEST, "batch_limit_exceeded");
	}
}
