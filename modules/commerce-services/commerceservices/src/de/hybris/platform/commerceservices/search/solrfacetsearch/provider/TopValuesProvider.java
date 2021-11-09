/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.solrfacetsearch.provider;

import de.hybris.platform.solrfacetsearch.provider.FacetTopValuesProvider;


/**
 * Top Values are a list of facet values that are immediately shown on search and category pages for facets with many
 * values. Other values will be collapsed.
 *
 * @deprecated Since 6.3, replaced by {@link FacetTopValuesProvider}
 */
@Deprecated(since = "6.3", forRemoval = true)
public interface TopValuesProvider extends FacetTopValuesProvider
{
	// Empty interface
}
