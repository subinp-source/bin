/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.solrfacetsearch.integration;

import static org.junit.Assert.assertEquals;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.constants.SolrfacetsearchConstants;
import de.hybris.platform.solrfacetsearch.solr.Index;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProvider;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;


public class IndexCreationTest extends AbstractIntegrationTest
{
	private static final Logger LOG = Logger.getLogger(IndexCreationTest.class);

	private static final int INDEX_COUNT = 10;
	private static final int DOCUMENT_COUNT = 25;

	@Test
	public void testCreateIndex() throws FacetConfigServiceException, SolrServiceException, SolrServerException, IOException
	{
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final SolrSearchProvider solrSearchProvider = getSolrSearchProviderFactory().getSearchProvider(facetSearchConfig,
				indexedType);

		for (int i = 0; i < INDEX_COUNT; i++)
		{
			final Index index = solrSearchProvider.resolveIndex(facetSearchConfig, indexedType, createIndexQualifier(i));

			LOG.info("Create index '" + index.getName() + "' ...");
			solrSearchProvider.createIndex(index);

			LOG.info("Index documents ...");

			final List<SolrInputDocument> documents = new ArrayList<>();

			for (int j = 0; j < DOCUMENT_COUNT; j++)
			{
				final SolrInputDocument document = new SolrInputDocument();
				document.setField(SolrfacetsearchConstants.ID_FIELD, "doc" + j);

				documents.add(document);
			}

			final SolrClient solrIndexingClient = solrSearchProvider.getClientForIndexing(index);
			solrIndexingClient.add(index.getName(), documents);
			solrIndexingClient.commit(index.getName(), true, true);

			LOG.info("Query index ...");

			final SolrClient solrClient = solrSearchProvider.getClient(index);
			final QueryResponse queryResponse = solrClient.query(index.getName(), new SolrQuery("*:*"));

			assertEquals(DOCUMENT_COUNT, queryResponse.getResults().getNumFound());
		}
	}

	protected String createIndexQualifier(final int i)
	{
		return Integer.toString(i);
	}
}
