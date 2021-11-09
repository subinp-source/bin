/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.solr.keywordredirect;

import de.hybris.platform.acceleratorservices.model.redirect.SolrPageRedirectModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commerceservices.search.solrfacetsearch.keywordredirect.KeywordRedirectUrlResolver;


/**
 * KeywordRedirectUrlResolver to create redirect URLs for WCMS pages
 */
public class PageKeywordRedirectUrlResolver implements KeywordRedirectUrlResolver<SolrPageRedirectModel>
{
	@Override
	public String resolve(final SolrPageRedirectModel redirect)
	{
		final AbstractPageModel page = redirect.getRedirectItem();

		if (page instanceof ContentPageModel)
		{
			return ((ContentPageModel) page).getLabel();
		}

		return null;
	}
}
