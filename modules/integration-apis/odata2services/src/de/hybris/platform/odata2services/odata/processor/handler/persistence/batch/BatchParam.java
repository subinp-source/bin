/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor.handler.persistence.batch;

import java.util.List;

import org.apache.olingo.odata2.api.batch.BatchHandler;
import org.apache.olingo.odata2.api.batch.BatchRequestPart;

/**
 * Defines the parameters needed to perform the batch persistence operation
 */
public interface BatchParam
{
	/**
	 * Gets the {@link BatchHandler}
	 * @return batch handler
	 */
	BatchHandler getBatchHandler();

	/**
	 * Gets the list of {@link BatchRequestPart}
	 * @return list of batch request parts, or empty list if none
	 */
	List<BatchRequestPart> getBatchRequestParts();

	/**
	 * Gets the number of batch request parts
	 * @return size of 0 or greater
	 */
	int getBatchRequestPartSize();
}
