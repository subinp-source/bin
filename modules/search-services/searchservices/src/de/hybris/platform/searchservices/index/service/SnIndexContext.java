/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.index.service;

import de.hybris.platform.searchservices.core.service.SnContext;


/**
 * Represents an index context.
 */
public interface SnIndexContext extends SnContext
{
	/**
	 * Returns the index id.
	 *
	 * @return the index id
	 */
	String getIndexId();

	/**
	 * Sets the index id.
	 *
	 * @param indexId
	 *           - the index id
	 */
	void setIndexId(final String indexId);
}
