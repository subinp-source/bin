/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.converters.populators;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.hybris.merchandising.model.FacetValue;
import com.hybris.merchandising.model.IndexedPropertyInfo;
import com.hybris.merchandising.model.Product;
import com.hybris.merchandising.model.ProductFacet;
import com.hybris.merchandising.model.ProductIndexContainer;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.FacetDisplayNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FacetValueDisplayNameProvider;
import de.hybris.platform.solrfacetsearch.search.FacetField;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;


/**
 * Populates {@link FacetPopulator} from {@link ProductIndexContainer} which encapsulates the information regarding the
 * indexed product details
 */
public class FacetPopulator implements Populator<ProductIndexContainer, Product>
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void populate(final ProductIndexContainer source, final Product target) throws ConversionException
	{
		final List<ProductFacet> facetFields = source.getMerchFacetPropertiesMapping().entrySet().stream().map(entry ->
		{
			final IndexedPropertyInfo indexedPropertyInfo = source.getIndexedPropertiesMapping().get(entry.getValue().getField());
			final IndexedProperty indexedProperty = indexedPropertyInfo.getIndexedProperty();
			final List<FacetValue> facetValues = createMerchFacetValues(entry.getValue(), source.getSearchQuery(), indexedProperty,
					source.getInputDocument(), indexedPropertyInfo);

			final ProductFacet facet = new ProductFacet();
			facet.setId(entry.getValue().getField());
			facet.setName(StringUtils.isEmpty(indexedProperty.getDisplayName()) ? facet.getId() : indexedProperty.getDisplayName());
			facet.setValues(facetValues);

			return facet;

		}).filter(Objects::nonNull).collect(Collectors.toList());

		target.setFacets(facetFields);
	}

	/**
	 * Helper method to retrieve the display name for a given facet value.
	 * @param searchQuery the {@link SearchQuery} being used for the current Solr indexing process.
	 * @param indexedProperty the {@link IndexedProperty} we wish to look up.
	 * @param facetDisplayNameProvider the display name provider, an instance of the class used to retrieve the display name of the facet.
	 * @param facetValue the value of the facet being used.
	 * @return the display name for the facet value, or the value itself if we can not derive it.
	 */
	protected String resolveFacetValueDisplayName(final SearchQuery searchQuery, final IndexedProperty indexedProperty,
			final Object facetDisplayNameProvider, final String facetValue)
	{
		return Optional.ofNullable(facetDisplayNameProvider)
				.filter(Objects::nonNull)
				.filter(valueNameProvider -> valueNameProvider instanceof FacetValueDisplayNameProvider)
				.map(valueNameProvider -> ((FacetValueDisplayNameProvider) valueNameProvider).getDisplayName(searchQuery,
						indexedProperty, facetValue))
				.orElseGet(() -> 
					Optional.ofNullable(resolveFacetValueDisplayNameUsingLegacyProvider(searchQuery, facetDisplayNameProvider, facetValue))
							.orElse(facetValue)
				);
	}


	/**
	 * resolveFacetValueDisplayNameUsingLegacyProvider is a method for retrieving the display name provider for a facet where the provider is an instance of
	 * the deprecated {@link FacetDisplayNameProvider}.
	 * @param searchQuery the {@link SearchQuery} being used for the current Solr indexing process.
	 * @param facetDisplayNameProvider the display name provider, an instance of the class used to retrieve the display name of the facet.
	 * @param facetValue the value of the facet being used.
	 * @return the display name for the facet value.
	 */
	protected String resolveFacetValueDisplayNameUsingLegacyProvider(final SearchQuery searchQuery, final Object facetDisplayNameProvider,
			final String facetValue)
	{
		return Optional.ofNullable(facetDisplayNameProvider)
				.filter(Objects::nonNull)
				.filter(valueNameProvider -> valueNameProvider instanceof FacetDisplayNameProvider)
				//Whilst the line below is deprecated, this is intentional to ensure we support FacetDisplayNameProvider
				//as well as FacetValueDisplayNameProvider. The reason being not to break the functional behaviour for the existing customers.
				.map(valueNameProvider -> ((FacetDisplayNameProvider) facetDisplayNameProvider).getDisplayName(searchQuery,
						facetValue))
				.orElse(null);
	}

	/**
	 * Utility method for retrieving a bean from the configured bean factory.
	 * @param facet
	 * @return
	 */
	protected static Object getFacetDisplayNameProvider(final FacetField facet)
	{
		return Optional.ofNullable(facet.getDisplayNameProvider()).map(name -> Registry.getCoreApplicationContext().getBean(name))
				.orElse(null);
	}

	/**
	 * Helper method to retrieve the values for a provided facet.
	 * @param facet the {@link FacetField} we wish to retrieve values for.
	 * @param query the {@link SearchQuery} being used for the current Solr indexing process.
	 * @param indexedProperty the {@link IndexedProperty} we wish to look up.
	 * @param document the {@link InputDocument} we wish to retrieve the value from.
	 * @param indexedPropertyInfo the {@link IndexedPropertyInfo} we retrieve the translated field name from.
	 * @return a List of {@link FacetValue} objects.
	 */
	protected List<FacetValue> createMerchFacetValues(final FacetField facet, final SearchQuery query,
			final IndexedProperty indexedProperty, final InputDocument document, final IndexedPropertyInfo indexedPropertyInfo)
	{
		final List<FacetValue> facetValues = Lists.newArrayList();

		final Object displayNameProvider = getFacetDisplayNameProvider(facet);

		final Object fieldValue = document.getFieldValue(indexedPropertyInfo.getTranslatedFieldName());

		if (fieldValue instanceof Collection)
		{
			@SuppressWarnings("unchecked")
			final Collection<Object> fieldValues = (Collection<Object>) fieldValue;

			fieldValues.stream()
					.map(fieldVal -> createFacetValueMapping(fieldVal, query, indexedProperty, displayNameProvider))
					.forEach(facetValues::add);
		}
		else if (fieldValue != null)
		{
			facetValues.add(createFacetValueMapping(fieldValue, query, indexedProperty, displayNameProvider));
		}

		return facetValues;

	}

	/**
	 * Helper method to create a {@link FacetValue} for a given facet.
	 * @param fieldValue a String representing the raw facet value.
	 * @param query the {@link SearchQuery} being used for the current Solr indexing process.
	 * @param indexedProperty the {@link IndexedProperty} we wish to look up.
	 * @param displayNameProvider the configured facet display name provider being used.
	 * @return an instance of {@link FacetValue} representing this facet.
	 */
	protected FacetValue createFacetValueMapping(final String fieldValue, final SearchQuery query,
			final IndexedProperty indexedProperty, final Object displayNameProvider)
	{
		return this.createFacetValueMapping((Object)fieldValue, query, indexedProperty, displayNameProvider);
	}

	/**
	 * Helper method to create a {@link FacetValue} for a given facet.
	 * @param fieldValue an Object representing the raw facet value.
	 * @param query the {@link SearchQuery} being used for the current Solr indexing process.
	 * @param indexedProperty the {@link IndexedProperty} we wish to look up.
	 * @param displayNameProvider the configured facet display name provider being used.
	 * @return an instance of {@link FacetValue} representing this facet.
	 */
	protected FacetValue createFacetValueMapping(final Object fieldValue, final SearchQuery query,
			final IndexedProperty indexedProperty, final Object displayNameProvider)
	{
		final FacetValue facetValue = new FacetValue();

		final String fieldDisplayValue = resolveFacetValueDisplayName(query, indexedProperty, displayNameProvider,
				String.valueOf(fieldValue));

		facetValue.setId(fieldValue);
		facetValue.setName(fieldDisplayValue);

		return facetValue;
	}
}
