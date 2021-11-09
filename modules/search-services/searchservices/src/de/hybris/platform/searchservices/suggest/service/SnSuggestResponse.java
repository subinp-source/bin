/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.suggest.service;

import de.hybris.platform.searchservices.core.service.SnResponse;
import de.hybris.platform.searchservices.suggest.data.SnSuggestResult;


/**
 * Represents a suggest response.
 */
public interface SnSuggestResponse extends SnResponse
{
	/**
	 * Returns the suggest result.
	 *
	 * @return the suggest result
	 */
	SnSuggestResult getSuggestResult();
}
