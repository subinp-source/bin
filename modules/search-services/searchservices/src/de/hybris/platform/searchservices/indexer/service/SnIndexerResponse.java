/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service;

import de.hybris.platform.searchservices.core.service.SnResponse;


/**
 * Represents an indexer response.
 */
public interface SnIndexerResponse extends SnResponse
{
	Integer getTotalItems();

	Integer getProcessedItems();
}
