/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.solrfacetsearch.strategies;

import de.hybris.platform.commerceservices.enums.SearchQueryContext;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;


/**
 * Resolves suitable search query templates names to be used in a specific context.
 */
public interface SearchQueryTemplateNameResolver
{
	/**
	 * Returns the search query template name to be used for a given {@link SearchQueryContext}.
	 *
	 * @param facetSearchConfig
	 *           - the facet search configuration
	 * @param indexedType
	 *           - the indexed type
	 * @param searchQueryContext
	 *           - the search query context
	 *
	 * @return the search query template name
	 */
	String resolveTemplateName(FacetSearchConfig facetSearchConfig, IndexedType indexedType,
			SearchQueryContext searchQueryContext);
}
