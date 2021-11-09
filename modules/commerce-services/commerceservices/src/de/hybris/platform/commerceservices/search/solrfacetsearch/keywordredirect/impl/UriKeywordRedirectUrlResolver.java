/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.solrfacetsearch.keywordredirect.impl;

import de.hybris.platform.commerceservices.search.solrfacetsearch.keywordredirect.KeywordRedirectUrlResolver;
import de.hybris.platform.solrfacetsearch.model.redirect.SolrURIRedirectModel;

/**
 * trivial KeywordRedirectUrlResolver for Redirects to static URLs
 */
public class UriKeywordRedirectUrlResolver implements KeywordRedirectUrlResolver<SolrURIRedirectModel>
{
	@Override
	public String resolve(final SolrURIRedirectModel redirect)
	{
		return redirect.getUrl();
	}
}
