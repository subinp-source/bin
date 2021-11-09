/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.suggest.service;

import de.hybris.platform.searchservices.suggest.SnSuggestException;

/**
 * Implementations of this interface are responsible for getting the applicable instance of {@link SnSuggestStrategy}.
 */
public interface SnSuggestStrategyFactory
{
	/**
	 * Returns an instance of {@link SnSuggestStrategy}.
	 *
	 * @param suggestRequest
	 *           - the suggest request
	 *
	 * @return the suggest strategy
	 *
	 * @throws SnSuggestException
	 *            if an error occurs
	 *
	 */
	SnSuggestStrategy getSuggestStrategy(SnSuggestRequest suggestRequest) throws SnSuggestException;
}
