/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.indexer.listeners;

import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hybris.merchandising.client.MerchCatalogServiceProductDirectoryClient;
import com.hybris.merchandising.exporter.MerchCategoryExporter;
import com.hybris.merchandising.function.FunctionWithExceptions;
import com.hybris.merchandising.model.MerchProductDirectoryConfigModel;
import com.hybris.merchandising.processor.ProductDirectoryProcessor;
import com.hybris.merchandising.service.MerchCatalogService;
import com.hybris.merchandising.service.MerchProductDirectoryConfigService;

import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.solrfacetsearch.config.IndexOperation;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchListener;
import de.hybris.platform.solrfacetsearch.indexer.IndexerContext;
import de.hybris.platform.solrfacetsearch.indexer.IndexerListener;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;


/**
 * DefaultMerchCatalogListener is a listener which listens to Solr indexing and handles synchronising
 * {@link MerchProductDirectoryConfigModel} definitions to CDS.
 */
public class DefaultMerchCatalogListener implements IndexerBatchListener, IndexerListener
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultMerchCatalogListener.class);
	protected static final String CATALOG_VERSION = "catalogVersion";
	protected static final String CATALOG_ID = "catalogId";

	private MerchProductDirectoryConfigService merchProductDirectoryConfigService;
	private MerchCatalogService merchCatalogService;
	private MerchCatalogServiceProductDirectoryClient merchCatalogServiceProductDirectoryClient;
	private MerchCategoryExporter merchCategoryExporter;
	private BaseSiteService baseSiteService;
	private ProductDirectoryProcessor productDirectoryProcessor;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beforeBatch(final IndexerBatchContext indexerBatchContext) throws IndexerException
	{
		//NOOP
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void afterBatch(final IndexerBatchContext indexerBatchContext) throws IndexerException
	{
		LOG.debug("after batch callback method for index invoked: {} ", indexerBatchContext.getIndexedType().getIdentifier());

		final Optional<MerchProductDirectoryConfigModel> productDirectory = merchProductDirectoryConfigService
				.getMerchProductDirectoryConfigForIndexedType(indexerBatchContext.getIndexedType().getIdentifier());

		productDirectory
				.filter(MerchProductDirectoryConfigModel::isEnabled)
				.map(FunctionWithExceptions
						.rethrowFunction(configModel -> merchCatalogService.getProducts(indexerBatchContext, configModel)))
				.ifPresent(products ->
				{
					productDirectory.ifPresent(directory ->
					{
						LOG.info("Products found to export to Merchandising: {}", products.size());
						//Set the base site to the first one associated with the product directory.
						productDirectory.get().getBaseSites().stream().findFirst()
								.ifPresent(site ->
								{
									LOG.info("afterBatch - setting current base site to: {}", site.getName());
									baseSiteService.setCurrentBaseSite(site, true);
									if (!products.isEmpty())
									{
										if (indexerBatchContext.getIndexOperation().equals(IndexOperation.FULL))
										{
											merchCatalogServiceProductDirectoryClient.handleProductsBatch(directory.getCdsIdentifier(),
													indexerBatchContext.getIndexOperationId(), products);
										}
										else
										{
											merchCatalogServiceProductDirectoryClient.handleProductsBatch(directory.getCdsIdentifier(),
													products);
										}
									}
								});
					});
				});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void afterBatchError(final IndexerBatchContext indexerBatchContext) throws IndexerException
	{
		//NOOP
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void afterIndex(final IndexerContext indexerContext) throws IndexerException
	{
		if (indexerContext.getIndexOperation().equals(IndexOperation.FULL))
		{
			final Optional<MerchProductDirectoryConfigModel> productDirectory = merchProductDirectoryConfigService
					.getMerchProductDirectoryConfigForIndexedType(indexerContext.getIndexedType().getIdentifier());
			productDirectory
					.filter(MerchProductDirectoryConfigModel::isEnabled)
					.ifPresent(directory ->
					{
						directory.getBaseSites().stream().findFirst()
								.ifPresent(site ->
								{
									LOG.info("afterIndex - setting base site to: {}", site.getName());
									baseSiteService.setCurrentBaseSite(site, true);
									merchCatalogServiceProductDirectoryClient.publishProducts(directory.getCdsIdentifier(),
											indexerContext.getIndexOperationId());
									merchCategoryExporter.exportCategories(directory);
								});
					});
		}
	}

	@Override
	public void beforeIndex(final IndexerContext context) throws IndexerException
	{
		final Optional<MerchProductDirectoryConfigModel> productDirectory = merchProductDirectoryConfigService
				.getMerchProductDirectoryConfigForIndexedType(context.getIndexedType().getIdentifier());

		productDirectory
				.filter(MerchProductDirectoryConfigModel::isEnabled)
				.filter(configModel -> StringUtils.isEmpty(configModel.getCdsIdentifier()))
				.ifPresent(configModel ->
				{
					LOG.info("updateMerchProductDirectory - Update the Product Directory for given indexType: {}",
							configModel.getIndexedType());
					productDirectoryProcessor.createUpdate(configModel);
				});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void afterIndexError(final IndexerContext indexerContext) throws IndexerException
	{
		// NOOP
	}

	public void setMerchProductDirectoryConfigService(
			final MerchProductDirectoryConfigService merchProductDirectoryConfigService)
	{
		this.merchProductDirectoryConfigService = merchProductDirectoryConfigService;
	}

	public void setMerchCatalogService(final MerchCatalogService merchCatalogService)
	{
		this.merchCatalogService = merchCatalogService;
	}

	public void setMerchCatalogServiceProductDirectoryClient(
			final MerchCatalogServiceProductDirectoryClient merchCatalogServiceProductDirectoryClient)
	{
		this.merchCatalogServiceProductDirectoryClient = merchCatalogServiceProductDirectoryClient;
	}

	public void setMerchCategoryExporter(final MerchCategoryExporter merchCategoryExporter)
	{
		this.merchCategoryExporter = merchCategoryExporter;
	}

	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	public void setProductDirectoryProcessor(final ProductDirectoryProcessor productDirectoryProcessor)
	{
		this.productDirectoryProcessor = productDirectoryProcessor;
	}
}
