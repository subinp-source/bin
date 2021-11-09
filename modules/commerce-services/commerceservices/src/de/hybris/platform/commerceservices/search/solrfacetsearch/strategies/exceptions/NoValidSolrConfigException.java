/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.exceptions;

import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.SolrFacetSearchConfigSelectionStrategy;


/**
 * Throws when {@link SolrFacetSearchConfigSelectionStrategy} cannot resolve suitable solr index config for the current
 * context.
 * 
 *
 * 
 */
public class NoValidSolrConfigException extends Exception
{

	/**
	 * @param message
	 */
	public NoValidSolrConfigException(final String message)
	{
		super(message);
	}

}
