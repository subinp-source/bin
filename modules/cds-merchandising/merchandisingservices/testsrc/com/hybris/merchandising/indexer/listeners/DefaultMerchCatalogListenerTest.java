/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.indexer.listeners;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.hybris.merchandising.exporter.MerchCategoryExporter;
import com.hybris.merchandising.model.MerchProductDirectoryConfigModel;
import com.hybris.merchandising.model.Product;
import com.hybris.merchandising.processor.ProductDirectoryProcessor;
import com.hybris.merchandising.service.MerchCatalogService;
import com.hybris.merchandising.service.MerchProductDirectoryConfigService;
import com.hybris.merchandising.client.MerchCatalogServiceProductDirectoryClient;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.solrfacetsearch.config.IndexOperation;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.IndexerContext;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;


@UnitTest
public class DefaultMerchCatalogListenerTest
{
	private static final String BATCH_INDEXER_ID = "batch001";
	private static final String PRODUCT_DIRECTORY_ID = "prod1";
	private static final Long INDEX_OPERATION_ID = 1L;

	@Mock
	private MerchProductDirectoryConfigService merchProductDirectoryConfigService;

	@Mock
	private MerchCatalogService merchCatalogService;

	@Mock
	private ProductDirectoryProcessor preIndexProcessor;

	@Mock
	private IndexerBatchContext indexerBatchContext;

	@Mock
	private IndexerContext indexerContext;

	@Mock
	private MerchProductDirectoryConfigModel merchProductDirectoryConfigModel;

	@Mock
	private MerchCatalogServiceProductDirectoryClient productDirectoryClient;

	@Mock
	private MerchCategoryExporter categoryExporter;

	@Mock
	private BaseSiteService mockBaseSiteService;

	@Mock
	private BaseSiteModel mockBaseSite;

	@Mock
	private Product mockProduct;

	@Mock
	private SolrIndexedTypeModel solrIndexedTypeModel;

	@InjectMocks
	private DefaultMerchCatalogListener defaultMerchCatalogListener;

	private List<Product> products;

	@Before
	public void setUp() throws IndexerException
	{

		MockitoAnnotations.initMocks(this);

		when(merchProductDirectoryConfigModel.getCdsIdentifier()).thenReturn(PRODUCT_DIRECTORY_ID);
		products = Collections.singletonList(mockProduct);
		when(merchProductDirectoryConfigService.getMerchProductDirectoryConfigForIndexedType(Matchers.anyString()))
				.thenReturn(Optional.of(merchProductDirectoryConfigModel));

		when(merchCatalogService.getProducts(indexerBatchContext, merchProductDirectoryConfigModel))
				.thenReturn(products);
		when(merchProductDirectoryConfigModel.isEnabled()).thenReturn(Boolean.TRUE);
		final IndexedType type = Mockito.mock(IndexedType.class);
		when(indexerBatchContext.getIndexOperationId()).thenReturn(INDEX_OPERATION_ID);
		when(indexerBatchContext.getIndexedType()).thenReturn(type);
		when(type.getIdentifier()).thenReturn(BATCH_INDEXER_ID);
		when(indexerContext.getIndexedType()).thenReturn(type);

		mockBaseSite = Mockito.mock(BaseSiteModel.class);
		when(mockBaseSite.getName()).thenReturn("apparel-uk");
		when(merchProductDirectoryConfigModel.getBaseSites()).thenReturn(Collections.singletonList(mockBaseSite));
	}

	@Test
	public void testAfterBatchFull() throws IndexerException
	{
		when(indexerBatchContext.getIndexOperation()).thenReturn(IndexOperation.FULL);
		defaultMerchCatalogListener.afterBatch(indexerBatchContext);
		verify(merchProductDirectoryConfigService, times(1)).getMerchProductDirectoryConfigForIndexedType(Matchers.anyString());
		verify(merchCatalogService).getProducts(indexerBatchContext, merchProductDirectoryConfigModel);
		verify(mockBaseSiteService).setCurrentBaseSite(mockBaseSite, Boolean.TRUE);
		verify(productDirectoryClient).handleProductsBatch(PRODUCT_DIRECTORY_ID, INDEX_OPERATION_ID, products);
	}

	@Test
	public void testAfterBatchPartial() throws IndexerException
	{
		when(indexerBatchContext.getIndexOperation()).thenReturn(IndexOperation.PARTIAL_UPDATE);
		defaultMerchCatalogListener.afterBatch(indexerBatchContext);
		verify(merchProductDirectoryConfigService, times(1)).getMerchProductDirectoryConfigForIndexedType(Matchers.anyString());
		verify(merchCatalogService).getProducts(indexerBatchContext, merchProductDirectoryConfigModel);
		verify(mockBaseSiteService).setCurrentBaseSite(mockBaseSite, Boolean.TRUE);
		verify(productDirectoryClient).handleProductsBatch(PRODUCT_DIRECTORY_ID, products);
	}

	@Test
	public void testAfterIndexFull() throws IndexerException
	{
		when(indexerContext.getIndexOperation()).thenReturn(IndexOperation.FULL);
		when(indexerContext.getIndexOperationId()).thenReturn(INDEX_OPERATION_ID);
		defaultMerchCatalogListener.afterIndex(indexerContext);
		verify(mockBaseSiteService).setCurrentBaseSite(mockBaseSite, Boolean.TRUE);
		verify(productDirectoryClient).publishProducts(PRODUCT_DIRECTORY_ID, INDEX_OPERATION_ID);
		verify(categoryExporter).exportCategories(merchProductDirectoryConfigModel);
	}

	@Test
	public void testAfterIndexPartial() throws IndexerException
	{
		when(indexerContext.getIndexOperation()).thenReturn(IndexOperation.PARTIAL_UPDATE);
		defaultMerchCatalogListener.afterIndex(indexerContext);
		verify(productDirectoryClient, never()).publishProducts(Matchers.anyString(), Matchers.anyLong());
		verify(categoryExporter, never()).exportCategories(merchProductDirectoryConfigModel);
	}

	@Test
	public void testBeforeIndexWithProductDirectoryId() throws IndexerException
	{
		defaultMerchCatalogListener.beforeIndex(indexerContext);

		verify(merchProductDirectoryConfigService, times(1)).getMerchProductDirectoryConfigForIndexedType(Matchers.anyString());
		verify(preIndexProcessor, never()).createUpdate(merchProductDirectoryConfigModel);
	}

	@Test
	public void testBeforeIndexWithNullProductDirectoryId() throws IndexerException
	{
		when(merchProductDirectoryConfigModel.getCdsIdentifier()).thenReturn(null);
		defaultMerchCatalogListener.beforeIndex(indexerContext);

		verify(merchProductDirectoryConfigService, times(1)).getMerchProductDirectoryConfigForIndexedType(Matchers.anyString());
		verify(preIndexProcessor, times(1)).createUpdate(merchProductDirectoryConfigModel);
	}

	@Test
	public void testBeforeIndexWithDisabledProductDirectoryId() throws IndexerException
	{
		defaultMerchCatalogListener.beforeIndex(indexerContext);

		verify(merchProductDirectoryConfigService, times(1)).getMerchProductDirectoryConfigForIndexedType(Matchers.anyString());
		verify(preIndexProcessor, never()).createUpdate(merchProductDirectoryConfigModel);
	}
}
