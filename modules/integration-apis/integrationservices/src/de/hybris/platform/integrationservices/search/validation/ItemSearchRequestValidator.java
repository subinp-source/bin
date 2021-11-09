/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.search.validation;

import de.hybris.platform.integrationservices.search.ItemSearchRequest;

/**
 * A validator for {@link ItemSearchRequest}s, which
 * ensures certain conditions in the request before performing an item search by
 * {@link de.hybris.platform.integrationservices.search.ItemSearchService}. If conditions are not met
 * the validator can throw an instance of {@link RuntimeException} and to veto the search.
 */
public interface ItemSearchRequestValidator
{
	/**
	 * Validates the specified item search request and rejects it by throwing an exception, if the
	 * specified request does not meet certain criteria.
	 *
	 * @param request a request to validate
	 * @throws RuntimeException if request is not valid and cannot be corrected.
	 */
	void validate(ItemSearchRequest request);
}
