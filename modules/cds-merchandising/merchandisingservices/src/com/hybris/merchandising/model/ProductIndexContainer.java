/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.model;

import java.util.Map;

import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.search.FacetField;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;


/**
 * POJO which encapsulates which encapsulates the information regarding the indexed product details
 * that is used to construct the {@ Product}.
 */
public class ProductIndexContainer
{
	private Map<String, IndexedPropertyInfo> indexedPropertiesMapping;
	private Map<String, FacetField> merchFacetPropertiesMapping;
	private Map<String, String> merchPropertiesMapping;
	private InputDocument inputDocument;
	private MerchProductDirectoryConfigModel merchProductDirectoryConfigModel;
	private SearchQuery searchQuery;

	public Map<String, IndexedPropertyInfo> getIndexedPropertiesMapping()
	{
		return indexedPropertiesMapping;
	}

	public Map<String, FacetField> getMerchFacetPropertiesMapping()
	{
		return merchFacetPropertiesMapping;
	}

	public Map<String, String> getMerchPropertiesMapping()
	{
		return merchPropertiesMapping;
	}

	public InputDocument getInputDocument()
	{
		return inputDocument;
	}

	public static ProductIndexContainerBuilder builder()
	{
		return new ProductIndexContainerBuilder();
	}

	public MerchProductDirectoryConfigModel getMerchProductDirectoryConfigModel()
	{
		return merchProductDirectoryConfigModel;
	}

	public SearchQuery getSearchQuery()
	{
		return searchQuery;
	}

	/**
	 * ProductIndexContainerBuilder is a builder class for generating an instance of
	 * {@link ProductIndexContainer}.
	 *
	 */
	public static class ProductIndexContainerBuilder
	{
		private Map<String, IndexedPropertyInfo> indexedPropertiesMapping;
		private Map<String, FacetField> merchFacetPropertiesMapping;
		private Map<String, String> merchPropertiesMapping;
		private InputDocument inputDocument;
		private MerchProductDirectoryConfigModel merchProductDirectoryConfigModel;
		private SearchQuery searchQuery;

		public ProductIndexContainerBuilder withIndexedPropertiesMapping(
				final Map<String, IndexedPropertyInfo> indexedPropertiesMapping)
		{
			this.indexedPropertiesMapping = indexedPropertiesMapping;
			return this;
		}

		public ProductIndexContainerBuilder withMerchFacetPropertiesMapping(
				final Map<String, FacetField> merchFacetPropertiesMapping)
		{
			this.merchFacetPropertiesMapping = merchFacetPropertiesMapping;
			return this;
		}

		public ProductIndexContainerBuilder withMerchPropertiesMapping(final Map<String, String> merchPropertiesMapping)
		{
			this.merchPropertiesMapping = merchPropertiesMapping;
			return this;
		}

		public ProductIndexContainerBuilder withInputDocument(final InputDocument inputDocument)
		{
			this.inputDocument = inputDocument;
			return this;
		}

		public ProductIndexContainerBuilder withMerchProductDirectoryConfigModel(
				final MerchProductDirectoryConfigModel merchProductDirectoryConfigModel)
		{
			this.merchProductDirectoryConfigModel = merchProductDirectoryConfigModel;
			return this;
		}

		public ProductIndexContainerBuilder withSearchQuery(final SearchQuery searchQuery)
		{
			this.searchQuery = searchQuery;
			return this;
		}

		public ProductIndexContainer build()
		{
			final ProductIndexContainer productIndexContainer = new ProductIndexContainer();

			productIndexContainer.indexedPropertiesMapping = indexedPropertiesMapping;
			productIndexContainer.merchFacetPropertiesMapping = merchFacetPropertiesMapping;
			productIndexContainer.merchPropertiesMapping = merchPropertiesMapping;
			productIndexContainer.inputDocument = inputDocument;
			productIndexContainer.merchProductDirectoryConfigModel = merchProductDirectoryConfigModel;
			productIndexContainer.searchQuery = searchQuery;

			return productIndexContainer;
		}
	}
}
