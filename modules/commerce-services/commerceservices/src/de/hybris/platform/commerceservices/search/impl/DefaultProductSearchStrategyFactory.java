/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.search.ProductSearchStrategy;
import de.hybris.platform.commerceservices.search.ProductSearchStrategyFactory;
import de.hybris.platform.commerceservices.search.exceptions.ProductSearchStrategyRuntimeException;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.AutocompleteSuggestion;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * Default implementation for {@link ProductSearchStrategyFactory}.
 */
public class DefaultProductSearchStrategyFactory<ITEM> implements ProductSearchStrategyFactory<ITEM>, ApplicationContextAware
{

	private ApplicationContext applicationContext;

	private BaseStoreService baseStoreService;

	private ProductSearchStrategy defaultProductSearchStrategy;

	@Override
	public ProductSearchStrategy<SolrSearchQueryData, ITEM, ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel>, AutocompleteSuggestion> getSearchStrategy()
	{
		final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();

		if (currentBaseStore == null)
		{
			throw new ProductSearchStrategyRuntimeException("The base store is null");
		}

		final String productSearchStrategy = currentBaseStore.getProductSearchStrategy();
		try
		{
			if (StringUtils.isNotBlank(productSearchStrategy))
			{
				return applicationContext.getBean(productSearchStrategy, ProductSearchStrategy.class);
			}
			else if (defaultProductSearchStrategy != null)
			{
				return defaultProductSearchStrategy;
			}
			else
			{
				throw new ProductSearchStrategyRuntimeException("Cannot retrieve default search product strategy");
			}
		}
		catch (final NoSuchBeanDefinitionException e)
		{
			throw new ProductSearchStrategyRuntimeException("Product search strategy [" + productSearchStrategy + "] not found!", e);
		}
	}

	protected ApplicationContext getApplicationContext()
	{
		return applicationContext;
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext)
	{
		this.applicationContext = applicationContext;
	}

	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	public ProductSearchStrategy getDefaultProductSearchStrategy()
	{
		return defaultProductSearchStrategy;
	}

	public void setDefaultProductSearchStrategy(final ProductSearchStrategy defaultProductSearchStrategy)
	{
		this.defaultProductSearchStrategy = defaultProductSearchStrategy;
	}

}
