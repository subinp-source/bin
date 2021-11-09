/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.solrfacetsearch.integration;

import de.hybris.bootstrap.annotations.IntegrationTest;


@IntegrationTest
public class SearchQueryFreeTextSearchDisMaxTest extends AbstractSearchQueryFreeTextSearchTest
{
	@Override
	protected String getFreeTextQueryBuilder()
	{
		return "disMaxFreeTextQueryBuilder";
	}
}
