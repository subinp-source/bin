/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.converters.populators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hybris.merchandising.exceptions.MerchandisingMetricRollupException;
import com.hybris.merchandising.metric.rollup.strategies.MerchandisingMetricRollupStrategy;
import com.hybris.merchandising.model.IndexedPropertyInfo;
import com.hybris.merchandising.model.Product;
import com.hybris.merchandising.model.ProductIndexContainer;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * Populates {@link ProductDetailsPopulator} from {@link ProductIndexContainer} which encapsulates the information regarding the
 * indexed product details.
 */
public class ProductDetailsPopulator implements Populator<ProductIndexContainer, Product>
{
	protected static final String CATEGORIES_KEY = "allCategories";
	protected static final String URL = "url";
	protected static final String CODE = "code";

	private List<MerchandisingMetricRollupStrategy> rollupStrategies;

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductDetailsPopulator.class);

	@Override
	public void populate(final ProductIndexContainer source, final Product target) throws ConversionException
	{
		target.setCategories(populateCategory(source));
		target.setPageUrl(populatePageUrl(source));
		target.setProductDetails(populateMerchProperties(source));
		target.setReportingGroup(getReportingGroup(source));
	}

	/**
	 * Helper method to populate product specific details that are to Merchandising properties.
	 * @param source the {@link ProductIndexContainer} representing the current Solr search and configuration being used.
	 * @return Map contains name value pair of Merchandising properties.
	 */
	protected Map<String, Object> populateMerchProperties(final ProductIndexContainer source)
	{
		// When we add additional mapping configuration to merchandising properties, then it doen't require
		// any code change to consider the new mapping fields, so we use Map instead of POJO
		return source.getMerchPropertiesMapping().entrySet().stream()
				.filter(entry -> !isImageField(entry.getValue()))
				.filter(entry -> !isMetadataField(entry.getValue()))
				.filter(Objects::nonNull)
				.collect(Collectors.toMap(Entry::getValue, entrySetValue ->
				{
					final IndexedPropertyInfo indexedPropertyInfo = source.getIndexedPropertiesMapping().get(entrySetValue.getKey());
					final String translatedFieldName = indexedPropertyInfo.getTranslatedFieldName();
					return Optional.ofNullable(source.getInputDocument().getFieldValue(translatedFieldName)).orElse("");
				}));
	}

	/**
	 * Helper method to populate list of categories that the product is in.
	 * @param source the {@link ProductIndexContainer} representing  the current Solr search and configuration being used.
	 * @return List of categories that the product is in.
	 */
	@SuppressWarnings("unchecked")
	protected List<String> populateCategory(final ProductIndexContainer source)
	{
		final IndexedPropertyInfo indexedPropertyInfo = source.getIndexedPropertiesMapping().get(CATEGORIES_KEY);
		final String translatedFieldName = indexedPropertyInfo.getTranslatedFieldName();
		source.getMerchPropertiesMapping().remove(CATEGORIES_KEY);
		final Object categories = source.getInputDocument().getFieldValue(translatedFieldName);
		if(categories instanceof String)
		{
			return Collections.singletonList((String) categories);
		}
		else if(categories instanceof List)
		{
			return (List<String>) categories;
		}
		LOGGER.warn("Unable to map categories. Returning empty list");
		return new ArrayList<>(0);
	}

	/**
	 * populateProductPageUrl is a method for retrieving the product page URL from the indexed document and
	 * appending the URL of the commerce instance to it.
	 * @param source the {@link ProductIndexContainer} representing the source.
	 * @return the page URL.
	 */
	protected String populatePageUrl(final ProductIndexContainer source)
	{
		final IndexedPropertyInfo indexedPropertyInfo = source.getIndexedPropertiesMapping().get(URL);
		final String translatedFieldName = Optional.ofNullable(indexedPropertyInfo.getTranslatedFieldName()).orElse("");
		source.getMerchPropertiesMapping().remove(URL);
		return  StringUtils.join(source.getMerchProductDirectoryConfigModel().getBaseCatalogPageUrl(),
				(String) source.getInputDocument().getFieldValue(translatedFieldName));
	}

	/**
	 * getReportingGroup is a method for retrieving the reporting group to use with the product, depending
	 * on how the product directory is configured to do variant rollup or not.
	 * @param source the {@link ProductIndexContainer} representing the source.
	 * @return the reporting group to use for the product.
	 */
	protected String getReportingGroup(final ProductIndexContainer source)
	{
		return rollupStrategies.stream()
				.filter(strategy -> strategy.getName().equals(source.getMerchProductDirectoryConfigModel().getRollUpStrategy()))
				.map(strategy -> {
					try {
						return strategy.getReportingGroup(source.getInputDocument(),source.getMerchProductDirectoryConfigModel().getRollUpStrategyField());
					} catch (final MerchandisingMetricRollupException e) {
						LOGGER.error("Exception caught when calculating reporting group", e);
						return (String) source.getInputDocument().getFieldValue(CODE);
					}
				})
				.findFirst().orElse((String) source.getInputDocument().getFieldValue(CODE));
	}

	/**
	 * isMetadataField is a utility method for deducing whether a field is a metadata field or not. If so
	 * the field should be handled by the {@link ProductMetadataPopulator}.
	 * @param key the field key.
	 * @return whether it is a metadata field or not.
	 */
	private boolean isMetadataField(final String key)
	{
		switch(key)
		{
			case ProductMetadataPopulator.DESCRIPTION_KEY:
			case ProductMetadataPopulator.NAME_KEY:
			case ProductMetadataPopulator.SUMMARY_KEY:
				return true;
			default:
				return false;
		}
	}

	/**
	 * isImageField is a simple method for verifying whether a field should be treated as an image field or not.
	 * @param key the field name.
	 * @return true if it is a main image or thumbnail image.
	 */
	private boolean isImageField(final String key)
	{
		return key.equals(ProductImagePopulator.MAIN_IMAGE) || key.equals(ProductImagePopulator.THUMBNAIL_IMAGE);
	}

	/**
	 * Retrieves the configured list of {@link MerchandisingMetricRollupStrategy} beans used for variant rollup.
	 * @return the list of roll up strategies.
	 */
	protected List<MerchandisingMetricRollupStrategy> getRollupStrategies() {
		return rollupStrategies;
	}

	/**
	 * Sets the configured list of {@link MerchandisingMetricRollupStrategy} beans used for variant rollup.
	 * @param rollupStrategies the list of strategies to set.
	 */
	public void setRollupStrategies(final List<MerchandisingMetricRollupStrategy> rollupStrategies) {
		this.rollupStrategies = rollupStrategies;
	}
}
