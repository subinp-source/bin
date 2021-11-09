/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.processor.handler.persistence;

import org.apache.olingo.odata2.api.ep.entry.ODataEntry;

/**
 * A validator that can be used to check the request parameters/payload before processing it.
 */
public interface RequestValidator
{
	/**
	 * Validates the request and throws some exception, if the request does not satisfy the condition imposed by this validator
	 * implementations. Remember, if your implementation throws an exception that is just a {@link RuntimeException}, it's necessary
	 * to create a corresponding {@link de.hybris.platform.odata2services.odata.errors.ErrorContextPopulator} and to register it
	 * in {@link de.hybris.platform.odata2services.odata.errors.CustomODataExceptionAwareErrorCallback} to convert that exception
	 * to a response. However, if the exception thrown by the implementation extends {@link org.apache.olingo.odata2.api.exception.ODataRuntimeApplicationException},
	 * then no error context populator is necessary, if the default conversion to a response based on the exception properties is
	 * good enough.
	 *
	 * @param param   request parameters
	 * @param path    OData entry containing only integration key attributes and representing the integration key specified in the
	 *                request URL.
	 * @param payload OData entry representing the request payload
	 */
	void validate(PersistenceParam param, ODataEntry path, ODataEntry payload);
}
