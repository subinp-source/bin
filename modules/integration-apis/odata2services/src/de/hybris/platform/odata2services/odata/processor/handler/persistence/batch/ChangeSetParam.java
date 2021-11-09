/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor.handler.persistence.batch;

import java.util.List;

import org.apache.olingo.odata2.api.batch.BatchHandler;
import org.apache.olingo.odata2.api.processor.ODataRequest;

/**
 * Defines the parameters needed to perform the change set persistence operation
 */
public interface ChangeSetParam
{
	/**
	 * Gets the {@link BatchHandler}
	 * @return batch handler
	 */
	BatchHandler getBatchHandler();

	/**
	 * Gets the list of {@link ODataRequest}s
	 * @return ODataRequests
	 */
	List<ODataRequest> getRequests();
}
