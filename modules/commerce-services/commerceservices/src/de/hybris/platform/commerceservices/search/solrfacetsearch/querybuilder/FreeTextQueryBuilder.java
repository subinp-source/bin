/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.solrfacetsearch.querybuilder;

import de.hybris.platform.solrfacetsearch.search.SearchQuery;


/**
 * Interface used by the DefaultSearchFacade to allow the free text query to be built from a number of beans.
 *
 * @deprecated Since 6.4, default search mode (instead of legacy) should be used.
 */
@Deprecated(since = "6.4", forRemoval = true)
public interface FreeTextQueryBuilder
{
	/**
	 * Add a free text query to the search query.
	 *
	 * @param searchQuery
	 *           The search query to add search terms to
	 * @param fullText
	 *           The full text of the query
	 * @param textWords
	 *           The full text query split into words
	 */
	void addFreeTextQuery(final SearchQuery searchQuery, final String fullText, final String[] textWords);
}
