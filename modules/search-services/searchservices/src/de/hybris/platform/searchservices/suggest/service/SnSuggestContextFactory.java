/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.suggest.service;

import de.hybris.platform.searchservices.suggest.SnSuggestException;

/**
 * Implementations of this interface are responsible for creating instances of {@link SnSuggestContext}.
 */
public interface SnSuggestContextFactory
{
	/**
	 * Creates a new instance of {@link SnSuggestContext}.
	 *
	 * @param suggestRequest
	 *           - the suggest request
	 *
	 * @return the new instance of {@link SnSuggestContext}
	 *
	 * @throws SnSuggestException
	 *            if an error occurs
	 */
	SnSuggestContext createSuggestContext(SnSuggestRequest suggestRequest) throws SnSuggestException;
}
