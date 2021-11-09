/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.solrfacetsearch.indexer.impl;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContextFactory;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.ExporterException;
import de.hybris.platform.solrfacetsearch.solr.Index;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProvider;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProviderFactory;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class SolrServerExporterTest
{
	private static final String INDEX_NAME = "index1";
	private static final String DOCUMENT_ID = "id1";

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Mock
	private IndexerBatchContextFactory<IndexerBatchContext> indexerBatchContextFactory;

	@Mock
	private SolrSearchProviderFactory solrSearchProviderFactory;

	@Mock
	private SolrSearchProvider solrSearchProvider;

	@Mock
	private IndexerBatchContext indexerBatchContext;

	@Mock
	private FacetSearchConfig facetSearchConfig;

	@Mock
	private IndexedType indexedType;

	@Mock
	private Index index;

	@Mock
	private SolrClient solrClient;

	private SolrServerExporter solrServerExporter;

	@Before
	public void setUp() throws SolrServiceException
	{
		MockitoAnnotations.initMocks(this);

		when(indexerBatchContextFactory.getContext()).thenReturn(indexerBatchContext);
		when(indexerBatchContext.getIndex()).thenReturn(index);
		when(index.getName()).thenReturn(INDEX_NAME);
		when(solrSearchProviderFactory.getSearchProvider(facetSearchConfig, indexedType)).thenReturn(solrSearchProvider);
		when(solrSearchProvider.getClientForIndexing(index)).thenReturn(solrClient);

		solrServerExporter = new SolrServerExporter();
		solrServerExporter.setSolrSearchProviderFactory(solrSearchProviderFactory);
		solrServerExporter.setIndexerBatchContextFactory(indexerBatchContextFactory);
	}

	@Test
	public void exportToUpdateWithEmptyDocuments() throws SolrServerException, ExporterException, IOException
	{
		// given
		final List<SolrInputDocument> documents = new ArrayList<>();

		// when
		solrServerExporter.exportToUpdateIndex(documents, facetSearchConfig, indexedType);

		// then
		verify(solrClient, never()).add(INDEX_NAME, documents);
	}

	@Test
	public void exportToUpdateWithDocuments() throws SolrServerException, ExporterException, IOException
	{
		// given
		final List<SolrInputDocument> documents = new ArrayList<>();
		documents.add(new SolrInputDocument());

		// when
		solrServerExporter.exportToUpdateIndex(documents, facetSearchConfig, indexedType);

		// then
		verify(solrClient, times(1)).add(INDEX_NAME, documents);
	}

	@Test
	public void exportToDeleteWithEmptyDocuments() throws SolrServerException, ExporterException, IOException
	{
		// given
		final List<String> ids = new ArrayList<>();

		// when
		solrServerExporter.exportToDeleteFromIndex(ids, facetSearchConfig, indexedType);

		// then
		verify(solrClient, never()).deleteById(INDEX_NAME, ids);
	}

	@Test
	public void exportToDeleteWithDocuments() throws SolrServerException, ExporterException, IOException
	{
		// given
		final List<String> ids = new ArrayList<>();
		ids.add(DOCUMENT_ID);

		// when
		solrServerExporter.exportToDeleteFromIndex(ids, facetSearchConfig, indexedType);

		// then
		verify(solrClient, times(1)).deleteById(INDEX_NAME, ids);
	}
}
