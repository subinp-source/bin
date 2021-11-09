/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.converters.populators;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hybris.merchandising.model.Product;
import com.hybris.merchandising.model.ProductIndexContainer;
import com.hybris.merchandising.model.ProductMetadata;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * Populates {@link ProductMetadataPopulator} from {@link ProductIndexContainer} which encapsulates the information regarding the
 * indexed product details
 */
public class ProductMetadataPopulator implements Populator<ProductIndexContainer, Product>
{
	protected static final String NAME_KEY = "name";
	protected static final String DESCRIPTION_KEY = "description";
	protected static final String SUMMARY_KEY = "summary";

	private static final Logger LOG = LoggerFactory.getLogger(ProductMetadataPopulator.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void populate(final ProductIndexContainer source, final Product target) throws ConversionException
	{
		// Metadata is considered based on indexLanguages that have been configured under SolrFacetSearchConfig.
		final Map<String, ProductMetadata> metadata = source.getSearchQuery().getFacetSearchConfig().getIndexConfig().getLanguages()
				.stream().map(language -> Pair.of(language.getIsocode(), buildMetaData(source, language)))
				.collect(Collectors.toMap(Pair::getKey, Pair::getValue));

		target.setMetadata(metadata);
	}

	/**
	 * Helper method to extract the merchandising properties based on given language
	 * @param source {@link ProductIndexContainer} representing the Solr index operation.
	 * @param language the {@link LanguageModel} we wish to retrieve products for.
	 * @param fieldName the name of the field we wish to retrieve.
	 * @return
	 */
	protected String extractFieldValue(final ProductIndexContainer source, final LanguageModel language,
			final String fieldName)
	{
		return source.getIndexedPropertiesMapping().get(fieldName).getTranslatedFieldNames().stream()
				.filter(translatedName -> language.getIsocode().equals(extractLanguage(translatedName)))
				.map(nameField -> (String) source.getInputDocument().getFieldValue(nameField))
				.filter(Objects::nonNull)
				.findFirst().orElse(StringUtils.EMPTY);
	}

	/**
	 * buildMetaData is a method for building an {@link ProductMetadata} object from the source object.
	 * @param source the {@link ProductIndexContainer} representing the Solr index operation.
	 * @param language the {@link LanguageModel} we wish to retrieve these fields for.
	 * @return the {@link ProductMetadata} object representing the metadata.
	 */
	protected ProductMetadata buildMetaData(final ProductIndexContainer source, final LanguageModel language)
	{
		final ProductMetadata productMetadata = new ProductMetadata();

		Optional.ofNullable(extractFieldValue(source, language, NAME_KEY)).ifPresent(name -> productMetadata.setName(sanitiseField(name)));
		Optional.ofNullable(extractFieldValue(source, language, SUMMARY_KEY)).ifPresent(summary -> productMetadata.setSummary(sanitiseField(summary)));
		Optional.ofNullable(extractFieldValue(source, language, DESCRIPTION_KEY)).ifPresent(description -> productMetadata.setDescription(sanitiseField(description)));

		return productMetadata;
	}

	/**
	 * Sanitise field is a method for URL encoding a field which may contain HTML characters.
	 * @param field the field to encode.
	 * @return the sanitised version.
	 */
	protected String sanitiseField(final String field)
	{
		if(StringUtils.isEmpty(field))
		{
			return field;
		}
		try 
		{
			return URLEncoder.encode(field, StandardCharsets.UTF_8.name());
		}
		catch(final UnsupportedEncodingException e)
		{
			LOG.error("Unable to URL encode field", e);
			return field;
		}
	}

	protected String extractLanguage(final String translatedField)
	{
		return StringUtils.substringAfterLast(translatedField, "_");
	}
}
