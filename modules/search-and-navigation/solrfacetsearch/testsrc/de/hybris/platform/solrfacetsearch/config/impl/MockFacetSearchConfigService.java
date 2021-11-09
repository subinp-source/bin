/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.solrfacetsearch.config.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigs;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfigs;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.IndexedTypes;
import de.hybris.platform.solrfacetsearch.config.ValueRange;
import de.hybris.platform.solrfacetsearch.config.ValueRangeSet;
import de.hybris.platform.solrfacetsearch.config.ValueRangeSets;
import de.hybris.platform.solrfacetsearch.config.ValueRanges;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.IdentityProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;


@Component
public class MockFacetSearchConfigService implements FacetSearchConfigService
{

	private Map<String, FacetSearchConfig> facetSearchConfigs;
	@Resource
	private CommonI18NService commonI18NService;
	@Resource
	private CatalogVersionService catalogVersionService;
	@Resource
	private TypeService typeService;
	@Resource
	private FieldValueProvider productPriceValueProvider;
	@Resource
	private FieldValueProvider modelPropertyFieldValueProvider;
	@Resource
	private IdentityProvider productIdentityProvider;

	@Override
	public FacetSearchConfig getConfiguration(final String name)
	{
		if (facetSearchConfigs == null)
		{
			facetSearchConfigs = createConfigs();
		}
		return facetSearchConfigs.get(name);
	}

	@Override
	public FacetSearchConfig getConfiguration(final CatalogVersionModel catalogVersion)
	{
		throw new UnsupportedOperationException("not supported for mock service");
	}

	@Override
	public IndexedType resolveIndexedType(final FacetSearchConfig facetSearchConfig, final String indexedTypeName)
			throws FacetConfigServiceException
	{
		throw new UnsupportedOperationException("not supported for mock service");
	}

	@Override
	public List<IndexedProperty> resolveIndexedProperties(final FacetSearchConfig facetSearchConfig, final IndexedType indexedType,
			final Collection<String> indexedPropertiesIds) throws FacetConfigServiceException
	{
		throw new UnsupportedOperationException("not supported for mock service");
	}

	protected Map<String, FacetSearchConfig> createConfigs()
	{
		final Map<String, FacetSearchConfig> configs = new HashMap<>(1);
		configs.put("productSearch", createPoductSearchConfig());
		return configs;
	}

	protected FacetSearchConfig createPoductSearchConfig()
	{
		final String name = "productSearch";
		final String description = "Product Search";
		final IndexConfig indexConfig = createProductIndexConfig();
		return FacetSearchConfigs.createFacetSearchConfig(name, description, indexConfig, null, null);
	}

	protected IndexConfig createProductIndexConfig()
	{
		final Collection<IndexedType> indexedTypes = createIndexedTypes();
		final Collection<CatalogVersionModel> catalogVersions = getCatalogVersions();
		final Collection<LanguageModel> languages = getLanguages();
		final Collection<CurrencyModel> currencies = getCurrencies();
		return IndexConfigs.createIndexConfig(indexedTypes, catalogVersions, languages, currencies, null, 100, 1, false);

	}

	protected Collection<CurrencyModel> getCurrencies()
	{
		return Arrays.asList(commonI18NService.getCurrency("EUR"), commonI18NService.getCurrency("USD"));
	}

	protected Collection<LanguageModel> getLanguages()
	{
		return Arrays.asList(commonI18NService.getLanguage("de"), commonI18NService.getLanguage("en"));
	}

	protected Collection<CatalogVersionModel> getCatalogVersions()
	{
		return Collections.singleton(catalogVersionService.getCatalogVersion("hwcatalog", "Online"));
	}

	protected Collection<IndexedType> createIndexedTypes()
	{
		return Collections.singleton(createProductIndexType());
	}

	protected IndexedType createProductIndexType()
	{
		final ComposedTypeModel composedType = typeService.getComposedTypeForCode("Product");
		return IndexedTypes.createIndexedType(composedType, false, createIndexedPropertiesForProduct(), null,
				"productIdentityProvider", null, null, null, "Product", null);
	}

	protected Collection<IndexedProperty> createIndexedPropertiesForProduct()
	{
		final List<IndexedProperty> properties = new ArrayList<>();
		properties.add(createIndexProperty("name", "string", false, null, true, modelPropertyFieldValueProvider));
		properties.add(createIndexProperty("onlineDate", "date", false, null, false, modelPropertyFieldValueProvider));

		final List<ValueRange> valueRangesEUR = new ArrayList<>();
		valueRangesEUR.add(ValueRanges.createValueRange("1-1000", Double.valueOf(1.0), Double.valueOf(1000.0)));
		valueRangesEUR.add(ValueRanges.createValueRange("1001-INF", Double.valueOf(1001.0), null));
		final ValueRangeSet valueRangeSetEUR = ValueRangeSets.createValueRangeSet("EUR", valueRangesEUR);

		final List<ValueRange> valueRangesUSD = new ArrayList<>();
		valueRangesUSD.add(ValueRanges.createValueRange("1-2000", Double.valueOf(1.0), Double.valueOf(2000.0)));
		valueRangesUSD.add(ValueRanges.createValueRange("2001-INF", Double.valueOf(2001.0), null));
		final ValueRangeSet valueRangeSetUSD = ValueRangeSets.createValueRangeSet("USD", valueRangesUSD);

		properties.add(createIndexProperty("price", "double", true, Arrays.asList(valueRangeSetEUR, valueRangeSetUSD), false,
				productPriceValueProvider));

		return properties;
	}

	protected IndexedProperty createIndexProperty(final String propertyName, final String type, final boolean facet,
			final List<ValueRangeSet> valueRangeSets, final boolean localized, final FieldValueProvider fieldValueProvider)
	{
		final IndexedProperty property = new IndexedProperty();
		property.setName(propertyName);
		property.setType(type);
		property.setFacet(facet);
		property.setExportId(propertyName);

		final Map valueRangeSets2 = new HashMap();
		if (valueRangeSets != null)
		{
			for (final ValueRangeSet valueRangeSet : valueRangeSets)
			{
				String qualifier = valueRangeSet.getQualifier();
				if (qualifier == null)
				{
					qualifier = "default";
				}
				valueRangeSets2.put(qualifier, valueRangeSet);
			}
		}

		property.setValueRangeSets(valueRangeSets2);
		property.setLocalized(localized);
		property.setFieldValueProvider("productPriceValueProvider");
		return property;
	}

	/**
	 * @param commonI18NService
	 *           the commonI18NService to set
	 */
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	/**
	 * @param catalogVersionService
	 *           the catalogVersionService to set
	 */
	public void setCatalogVersionService(CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	/**
	 * @param typeService
	 *           the typeService to set
	 */
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}

	/**
	 * @param productPriceValueProvider
	 *           the productPriceValueProvider to set
	 */
	public void setProductPriceValueProvider(final FieldValueProvider productPriceValueProvider)
	{
		this.productPriceValueProvider = productPriceValueProvider;
	}

	/**
	 * @param modelPropertyFieldValueProvider
	 *           the modelPropertyFieldValueProvider to set
	 */
	public void setModelPropertyFieldValueProvider(final FieldValueProvider modelPropertyFieldValueProvider)
	{
		this.modelPropertyFieldValueProvider = modelPropertyFieldValueProvider;
	}

	/**
	 * @param productIdentityProvider
	 *           the productIdentityProvider to set
	 */
	public void setProductIdentityProvider(final IdentityProvider productIdentityProvider)
	{
		this.productIdentityProvider = productIdentityProvider;
	}
}
