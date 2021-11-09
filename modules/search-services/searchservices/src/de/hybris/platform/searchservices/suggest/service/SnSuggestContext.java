/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.suggest.service;

import de.hybris.platform.searchservices.index.service.SnIndexContext;


/**
 * Represents a suggest context.
 */
public interface SnSuggestContext extends SnIndexContext
{
	/**
	 * Returns the suggest request.
	 */
	SnSuggestRequest getSuggestRequest();

	/**
	 * Returns the suggest response.
	 */
	SnSuggestResponse getSuggestResponse();

	/**
	 * Sets the suggest response.
	 *
	 * @param suggestResponse
	 *           - the suggest response
	 */
	void setSuggestResponse(final SnSuggestResponse suggestResponse);
}
