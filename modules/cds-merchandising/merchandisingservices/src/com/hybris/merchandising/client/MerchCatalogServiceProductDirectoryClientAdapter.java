/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.client;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hybris.charon.RawResponse;
import com.hybris.charon.exp.HttpException;
import com.hybris.merchandising.model.Product;
import com.hybris.merchandising.model.ProductDirectory;

import de.hybris.platform.apiregistryservices.exceptions.CredentialException;
import de.hybris.platform.apiregistryservices.services.ApiRegistryClientService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * MerchCatalogServiceClientAdapter is a client for the Merchandising Catalog Service to synchronise product directories,
 * products and categories from commerce.
 *
 */
public class MerchCatalogServiceProductDirectoryClientAdapter implements MerchCatalogServiceProductDirectoryClient
{
	private static final ObjectMapper objectMapper = new ObjectMapper();
	private ApiRegistryClientService apiRegistryClientService;
	private final Scheduler scheduler = Schedulers.io();
	private static final Logger LOG = LoggerFactory.getLogger( MerchCatalogServiceProductDirectoryClientAdapter.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String handleProductsBatch(final String productDirectoryId, final Long version, final List<Product> products) {
		return handleProductsBatchAsync(productDirectoryId, version, products)
				.subscribeOn(scheduler)
				.doOnError(throwable -> ClientExceptionHandler.handleExceptions(throwable, this.getClass()))
				.toBlocking().first();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Void handleProductsBatch(final String productDirectoryId, final List<Product> products) {
		return handleProductsBatchAsync(productDirectoryId, products)
				.subscribeOn(scheduler)
				.doOnError(throwable -> ClientExceptionHandler.handleExceptions(throwable, this.getClass()))
				.toBlocking().first();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Observable<Void> handleProductsBatchAsync(final String productDirectoryId, final List<Product> products) {
		if(LOG.isDebugEnabled()) {
			try {
				LOG.debug("Sending products to Catalog service for productDirectoryId: {}", productDirectoryId);
				LOG.debug(objectMapper.writeValueAsString(products));
			} catch (final JsonProcessingException e) {
				LOG.error("An error occurred serializing products to string", e);
			}
		}
		return getAdaptee().handleProductsBatchAsync(productDirectoryId, products).firstOrDefault(null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Observable<String> handleProductsBatchAsync(final String productDirectoryId, final Long version,
			final List<Product> products) {
		if(LOG.isDebugEnabled()) {
			try {
				LOG.debug("Sending products to Catalog service for productDirectoryId: {} and version:{}", productDirectoryId, version);
				LOG.debug(objectMapper.writeValueAsString(products));
			} catch (final JsonProcessingException e) {
				LOG.error("An error occurred serializing products to string", e);
			}
		}
		return getAdaptee().handleProductsBatchAsync(productDirectoryId, version, products);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Void publishProducts(final String productDirectoryId, final Long version) {
		return publishProductsAsync(productDirectoryId, version)
				.subscribeOn(scheduler)
				.doOnError(throwable -> ClientExceptionHandler.handleExceptions(throwable, this.getClass()))
				.toBlocking().firstOrDefault(null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Observable<Void> publishProductsAsync(final String productDirectoryId, final Long version) {
		return getAdaptee().publishProductsAsync(productDirectoryId, version);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Observable<String> handleCategoriesAsync(final String productDirectoryId,
			final CategoryHierarchyWrapper categories) {
		return getAdaptee().handleCategoriesAsync(productDirectoryId, categories);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String handleCategories(final String productDirectoryId,
			final CategoryHierarchyWrapper categories) {
		return handleCategoriesAsync(productDirectoryId, categories)
				.subscribeOn(scheduler)
				.toBlocking().first();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Observable<RawResponse> createProductDirectoryAsync(final ProductDirectory productDirectory)
	{
		return getAdaptee().createProductDirectoryAsync(productDirectory);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RawResponse createProductDirectory(final ProductDirectory productDirectory)
	{
		return createProductDirectoryAsync(productDirectory)
				.subscribeOn(scheduler)
				.doOnError(throwable -> ClientExceptionHandler.handleExceptions(throwable, this.getClass()))
				.toBlocking().first();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Observable<Void> updateProductDirectoryAsync(final String productDirectoryId, final ProductDirectory productDirectory)
	{
		return getAdaptee().updateProductDirectoryAsync(productDirectoryId, productDirectory);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Void updateProductDirectory(final String productDirectoryId, final ProductDirectory productDirectory)
	{
		return updateProductDirectoryAsync(productDirectoryId, productDirectory)
				.subscribeOn(scheduler)
				.doOnError(throwable -> ClientExceptionHandler.handleExceptions(throwable, this.getClass()))
				.toBlocking()
				.firstOrDefault(null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Observable<Void> deleteProductDirectoryAsync(final String productDirectoryId) {
			return getAdaptee().deleteProductDirectoryAsync(productDirectoryId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Void deleteProductDirectory(final String productDirectoryId) {
		try {
			return deleteProductDirectoryAsync(productDirectoryId)
					.subscribeOn(scheduler)
					.doOnError(throwable -> ClientExceptionHandler.handleExceptions(throwable, this.getClass()))
					.toBlocking()
					.firstOrDefault(null);
		}
		catch(final HttpException e)
		{
			LOG.warn("Product directory:{} to delete not found.", productDirectoryId, e);
			return null;
		}
	}

	/**
	 * Gets the configured service.
	 * @return the configured {@link MerchCatalogServiceProductDirectoryClient}.
	 */
	public MerchCatalogServiceProductDirectoryClient getAdaptee()
	{		
		try
		{
			return apiRegistryClientService.lookupClient(MerchCatalogServiceProductDirectoryClient.class);
		}
		catch (final CredentialException | ModelNotFoundException e)
		{
			LOG.error("Unable to retrieve client for MerchCatalogService. Unable to synchronize products. Please create a client if you wish to use Merchandising functionality.", e);
		}
		return null;		
	}

	/**
	 * Gets the configured client service.
	 * @return the configured {@link ApiRegistryClientService}.
	 */
	protected ApiRegistryClientService getApiRegistryClientService()
	{
		return apiRegistryClientService;
	}

	/**
	 * Sets the configured client service.
	 * @param apiRegistryClientService {@link ApiRegistryClientService}.
	 */
	public void setApiRegistryClientService(final ApiRegistryClientService apiRegistryClientService)
	{
		this.apiRegistryClientService = apiRegistryClientService;
	}
}
