/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.impl;


import de.hybris.platform.commerceservices.enums.SearchQueryContext;
import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.SearchQueryTemplateNameResolver;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;


/**
 * Default implementation of {@link SearchQueryTemplateNameResolver}.
 */
public class DefaultSearchQueryTemplateNameResolver implements SearchQueryTemplateNameResolver
{
	@Override
	public String resolveTemplateName(final FacetSearchConfig facetSearchConfig, final IndexedType indexedType,
			final SearchQueryContext searchQueryContext)
	{
		if (searchQueryContext != null)
		{
			return searchQueryContext.getCode().toUpperCase();
		}

		return SearchQueryContext.DEFAULT.getCode();
	}
}
