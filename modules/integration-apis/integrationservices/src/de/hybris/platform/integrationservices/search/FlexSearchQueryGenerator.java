/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.search;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

/**
 * Service to build flex search query based on an {@code ItemSearchRequest} sent as a parameter
 */
public interface FlexSearchQueryGenerator
{
	/**
	 * Creates the  {@code FlexibleSearchQuery} from the request sent as a parameter.
	 *
	 * @param searchRequest {@code ItemSearchRequest} with the information about the query parameters
	 * @return {@code FlexibleSearchQuery} with the query containing all the parameters resolved to PK's if needed
	 */
	FlexibleSearchQuery generate(ItemSearchRequest searchRequest);
}
