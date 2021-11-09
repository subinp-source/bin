/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor.handler;

import org.apache.olingo.odata2.api.exception.ODataException;

/**
 * An ODataProcessorHandler handles an operation defined in the {@link org.apache.olingo.odata2.api.processor.ODataSingleProcessor},
 * such as {@code createEntity}.
 *
 * @param <P> Parameter type
 * @param <R> Return type
 */
public interface ODataProcessorHandler<P, R>
{
	R handle(P param) throws ODataException;
}
