/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.model;

import java.util.List;

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;


/**
 * POJO which encapsulates IndexedProperty information that is used to construct the {@ Product}
 */
public class IndexedPropertyInfo
{
	private IndexedProperty indexedProperty;
	private List<String> translatedFieldNames;
	private String translatedFieldName;
	private boolean localised;

	public IndexedProperty getIndexedProperty()
	{
		return indexedProperty;
	}

	public List<String> getTranslatedFieldNames()
	{
		return translatedFieldNames;
	}

	public String getTranslatedFieldName()
	{
		return translatedFieldName;
	}

	public boolean isLocalised()
	{
		return localised;
	}

	public static IndexedPropertyInfoBuilder builder()
	{
		return new IndexedPropertyInfoBuilder();
	}

	public static class IndexedPropertyInfoBuilder
	{
		private IndexedProperty indexedProperty;
		private List<String> translatedFieldNames;
		private String translatedFieldName;
		private boolean localised;

		public IndexedPropertyInfoBuilder withIndexedProperty(final IndexedProperty indexedProperty)
		{
			this.indexedProperty = indexedProperty;
			return this;
		}

		public IndexedPropertyInfoBuilder withTranslatedFieldNames(final List<String> translatedFieldNames)
		{
			this.translatedFieldNames = translatedFieldNames;
			return this;
		}

		public IndexedPropertyInfoBuilder withTranslatedFieldName(final String translatedFieldName)
		{
			this.translatedFieldName = translatedFieldName;
			return this;
		}

		public IndexedPropertyInfoBuilder withLocalised(final boolean localised)
		{
			this.localised = localised;
			return this;
		}

		public IndexedPropertyInfo build()
		{
			final IndexedPropertyInfo indexedPropertyInfo = new IndexedPropertyInfo();
			indexedPropertyInfo.indexedProperty = indexedProperty;
			indexedPropertyInfo.translatedFieldNames = translatedFieldNames;
			indexedPropertyInfo.translatedFieldName = translatedFieldName;
			indexedPropertyInfo.localised = localised;

			return indexedPropertyInfo;
		}
	}
}
