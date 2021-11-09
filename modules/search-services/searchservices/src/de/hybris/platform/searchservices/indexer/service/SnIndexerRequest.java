/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service;

import de.hybris.platform.searchservices.core.service.SnRequest;
import de.hybris.platform.searchservices.enums.SnIndexerOperationType;

import java.util.List;


/**
 * Represents an indexer request.
 */
public interface SnIndexerRequest extends SnRequest
{
	/**
	 * Returns the indexer operation type.
	 *
	 * @return the indexer operation type
	 */
	SnIndexerOperationType getIndexerOperationType();

	/**
	 * Returns the indexer item source operations.
	 *
	 * @return the indexer item source operations
	 */
	List<SnIndexerItemSourceOperation> getIndexerItemSourceOperations();
}
