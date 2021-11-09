/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.search.impl;

import de.hybris.platform.integrationservices.search.FlexibleSearchQueryBuilder;
import de.hybris.platform.integrationservices.search.ItemSearchRequest;

/**
 * A factory for creating a {@link FlexibleSearchQueryBuilder}
 */
public interface FlexSearchQueryBuilderFactory
{
	/**
	 * Creates instance of the query builder and initializes it based on the specified search request values.
	 * @param searchRequest an item search request to apply to the created builder conditions.
	 * @return a new instance of the query builder
	 */
	FlexibleSearchQueryBuilder createQueryBuilder(ItemSearchRequest searchRequest);
}
