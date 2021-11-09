/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hybris.charon.RawResponse;
import com.hybris.merchandising.model.Product;
import com.hybris.merchandising.model.ProductDirectory;

import de.hybris.platform.commercefacades.catalog.data.CatalogVersionData;
import rx.Observable;

/**
 * Non operational debug utility implementation of {@link MerchCatalogServiceProductDirectoryClient}.
 *
 */
public class NoOpMerchCatalogServiceProductDirectoryClientAdapter implements MerchCatalogServiceProductDirectoryClient {
	private static final ObjectMapper objectMapper = new ObjectMapper();
	private static final Logger LOG = LoggerFactory.getLogger(NoOpMerchCatalogServiceProductDirectoryClientAdapter.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String handleProductsBatch(final String productDirectoryId, final Long version, final List<Product> products)
	{
		LOG.debug("handleProductsBatch invoked");
		return processBatch(productDirectoryId, version, products);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Void handleProductsBatch(final String productDirectoryId, final List<Product> products)
	{
		LOG.debug("handleProductsBatch invoked");
		processBatch(productDirectoryId, null, products);
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Observable<Void> handleProductsBatchAsync(final String productDirectoryId, final List<Product> products)
	{
		LOG.debug("handleProductsBatchAsynch invoked");
		processBatch(productDirectoryId, null, products);
		return Observable.empty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Observable<String> handleProductsBatchAsync(final String productDirectoryId, final Long version,
			final List<Product> products)
	{
		LOG.debug("handleProductsBatchAsynch invoked");
		return Observable.just(processBatch(productDirectoryId, version, products));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Void publishProducts(final String productDirectoryId, final Long version)
	{
		processPublish(productDirectoryId, version);
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Observable<Void> publishProductsAsync(final String productDirectoryId, final Long version)
	{
		processPublish(productDirectoryId, version);
		return Observable.empty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Observable<String> handleCategoriesAsync(final String productDirectoryId, 
			final CategoryHierarchyWrapper categories)
	{
		return Observable.just(processCategories(productDirectoryId, categories));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String handleCategories(final String productDirectoryId, 
			final CategoryHierarchyWrapper categories) {
		return processCategories(productDirectoryId, categories);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RawResponse createProductDirectory(final ProductDirectory productDirectory)
	{
		debugObject("Creating product directory", productDirectory);
		final RawResponse mockedRawResponse = Mockito.mock(RawResponse.class);
		Optional<URL> location = Optional.empty();
		try
		{
			location = Optional.of(new URL("https://localhost:9002/" + UUID.randomUUID().toString()));
		} catch (final MalformedURLException e)
		{
			LOG.error("Exception thrown when generating mock URL", e);
		}
		Mockito.when(mockedRawResponse.location()).thenReturn(location);
		return mockedRawResponse;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Void updateProductDirectory(final String productDirectoryId, final ProductDirectory productDirectory)
	{
		debugObject(String.format("Updating product directory: %s", productDirectoryId), productDirectory);
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Observable<RawResponse> createProductDirectoryAsync(final ProductDirectory productDirectory)
	{
		return Observable.just(createProductDirectory(productDirectory));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Observable<Void> updateProductDirectoryAsync(final String productDirectoryId, final ProductDirectory productDirectory)
	{
		debugObject(String.format("Updating product directory async: %s", productDirectoryId), productDirectory);
		return Observable.empty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Observable<Void> deleteProductDirectoryAsync(final String productDirectoryId)
	{
		LOG.debug("deleteProductDirectoryAsync invoked. Deleting productDirectory: {}", productDirectoryId);
		return Observable.empty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Void deleteProductDirectory(final String productDirectoryId)
	{
		LOG.debug("deleteProductDirectory invoked. Deleting productDirectory: {}", productDirectoryId);
		return null;
	}

	/**
	 * processCategories is a method for processing a collection of categories.
	 * @param categories a set of categories, wrapped in a {@link CatalogVersionData} entity.
	 * @return Category structure ID.
	 */
	private String processCategories(final String productDirectoryId,
			final CategoryHierarchyWrapper categories)
	{
		LOG.debug("Processing categories for productDirectoryId: {}", productDirectoryId);
		debugObject("", categories);
		return UUID.randomUUID().toString();
	}

	/**
	 * processPublish is a method for handling the publish operation.
	 * @param productDirectoryId the product directory ID to publish.
	 * @param version the version to publish.
	 */
	private void processPublish(final String productDirectoryId, final Long version)
	{
		LOG.debug("Publishing productDirectoryId: {}, version: {}", productDirectoryId, version);
	}

	/**
	 * processBatch is a method for handling the batch operation.
	 * @param productDirectoryId the ID of the product directory we are publishing.
	 * @param version the version to send the batch as.
	 * @param products the products to send.
	 * @return a list of responses.
	 */
	private String processBatch(final String productDirectoryId,
			final Long version, final List<Product> products)
	{
		if(version != null) {
			LOG.debug("handleProductsBatch invoked. productDirectoryId:{}, version:{}", 
					productDirectoryId, version);
		}
		products.forEach(product -> {
			debugObject("productReceived", product);
		});
		return UUID.randomUUID().toString();
	}

	private void debugObject(final String debugMessage, final Object input)
	{
		LOG.debug(debugMessage);
		if(LOG.isDebugEnabled())
		{
			try 
			{
				LOG.debug(objectMapper.writeValueAsString(input));
			} catch (final JsonProcessingException e) 
			{
				LOG.error("An error occurred serializing input", e);
			}
		}
	}
}
