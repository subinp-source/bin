/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.solrfacetsearch.indexer.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.integration.AbstractIntegrationTest;
import de.hybris.platform.solrfacetsearch.search.FacetSearchException;
import de.hybris.platform.solrfacetsearch.search.FacetSearchService;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResult;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;

import java.io.IOException;
import java.util.Collections;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Test;


public class IndexationInLanguageContextTest extends AbstractIntegrationTest
{
	@Resource
	private ProductService productService;

	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private CommonI18NService commonI18NService;

	@Resource
	private DefaultIndexerService indexerService;

	@Resource
	private FacetSearchService facetSearchService;


	private ProductModel product;

	@Override
	protected void loadData() throws ImpExException, IOException, FacetConfigServiceException, SolrServiceException,
			SolrServerException
	{
		importConfig("/test/integration/IndexationInLanguageContextTest.csv");

		product = productService.getProductForCode("product" + getTestId());
	}

	@Test
	public void testIndexInEnglish() throws IndexerException, SolrServiceException, SolrServerException, IOException,
			FacetSearchException, FacetConfigServiceException
	{
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("hwcatalog", "Online" + getTestId());

		commonI18NService.setCurrentLanguage(commonI18NService.getLanguage("en"));
		indexerService.performFullIndex(facetSearchConfig);

		SearchQuery query = new SearchQuery(facetSearchConfig, indexedType);
		query.setCatalogVersions(Collections.singletonList(catalogVersion));

		query.addQuery("name", "english");

		SearchResult search = facetSearchService.search(query);
		org.fest.assertions.Assertions.assertThat(search.getResults()).containsOnly(product);

		query = new SearchQuery(facetSearchConfig, indexedType);
		query.setCatalogVersions(Collections.singletonList(catalogVersion));
		
		query.addQuery("name", "deutches");

		search = facetSearchService.search(query);
		org.fest.assertions.Assertions.assertThat(search.getResults()).isEmpty();
	}

	@Test
	public void testIndexInGerman() throws IndexerException, SolrServiceException, SolrServerException, IOException,
			FacetSearchException, FacetConfigServiceException
	{
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("hwcatalog", "Online" + getTestId());

		commonI18NService.setCurrentLanguage(commonI18NService.getLanguage("de"));
		indexerService.performFullIndex(facetSearchConfig);

		SearchQuery query = new SearchQuery(facetSearchConfig, indexedType);
		query.setCatalogVersions(Collections.singletonList(catalogVersion));

		query.addQuery("name", "english");

		SearchResult search = facetSearchService.search(query);
		org.fest.assertions.Assertions.assertThat(search.getResults()).isEmpty();

		query = new SearchQuery(facetSearchConfig, indexedType);
		query.setCatalogVersions(Collections.singletonList(catalogVersion));
		query.addQuery("name", "deutches");

		search = facetSearchService.search(query);
		org.fest.assertions.Assertions.assertThat(search.getResults()).containsOnly(product);
	}
}
