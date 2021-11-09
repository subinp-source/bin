/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.solrfacetsearch.keywordredirect;

import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.solrfacetsearch.model.redirect.SolrAbstractKeywordRedirectModel;


/**
 * URL Resolver - constructs an actual URL from a Solr Keyword redirect model referencing a target of a particular type
 * (e.g. Product, Page, ...)
 */
public interface KeywordRedirectUrlResolver<T extends SolrAbstractKeywordRedirectModel> extends UrlResolver<T>
{
	//Empty
}
