/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.suggest.service;

import de.hybris.platform.searchservices.core.service.SnRequest;
import de.hybris.platform.searchservices.suggest.data.SnSuggestQuery;


/**
 * Represents a suggest request.
 */
public interface SnSuggestRequest extends SnRequest
{
	/**
	 * Returns the suggest query.
	 *
	 * @return the suggest query
	 */
	SnSuggestQuery getSuggestQuery();
}
