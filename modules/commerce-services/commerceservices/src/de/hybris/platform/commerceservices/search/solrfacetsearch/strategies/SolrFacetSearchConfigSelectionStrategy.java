/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.solrfacetsearch.strategies;

import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.exceptions.NoValidSolrConfigException;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;



/**
 * Resolves suitable {@link SolrFacetSearchConfigModel} that should be used for searching in the current session
 * context.<br>
 * 
 *
 * 
 */
public interface SolrFacetSearchConfigSelectionStrategy
{

	/**
	 * Resolves suitable {@link SolrFacetSearchConfigModel} that should be used for searching in the current session
	 * 
	 * @return {@link SolrFacetSearchConfigModel}
	 * @throws NoValidSolrConfigException
	 */
	SolrFacetSearchConfigModel getCurrentSolrFacetSearchConfig() throws NoValidSolrConfigException;

}
