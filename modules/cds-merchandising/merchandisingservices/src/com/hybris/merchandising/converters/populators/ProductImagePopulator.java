/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.converters.populators;

import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.hybris.merchandising.model.Product;
import com.hybris.merchandising.model.ProductImage;
import com.hybris.merchandising.model.ProductIndexContainer;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * Populates {@link ProductImagePopulator} from {@link ProductIndexContainer} which encapsulates the information regarding the
 * indexed product details
 */
public class ProductImagePopulator implements Populator<ProductIndexContainer, Product>
{
	protected static final String THUMBNAIL_IMAGE = "thumbnailImage";
	protected static final String MAIN_IMAGE = "mainImage";

	@Override
	public void populate(final ProductIndexContainer source, final Product target) throws ConversionException
	{
		final ProductImage image = new ProductImage();

		populateProductImage(source, THUMBNAIL_IMAGE).ifPresent(image::setThumbnailImage);
		populateProductImage(source, MAIN_IMAGE).ifPresent(image::setMainImage);

		target.setImages(image);
	}

	/**
	 * Helper method to populate image information associated with the product.
	 * @param source the {@link ProductIndexContainer} representing the Solr index.
	 * @param propertyName the property name being retrieved from the index.
	 * @return image information for the given key
	 */
	protected Optional<String> populateProductImage(final ProductIndexContainer source,
			final String propertyName)
	{
		return source.getMerchPropertiesMapping().entrySet().stream()
				.filter(entry -> propertyName.equals(entry.getValue()))
				.map(requiredEntry -> source.getIndexedPropertiesMapping().get(requiredEntry.getKey()))
				.map(entry -> {
					return entry;
				})
				.filter(Objects::nonNull)
				.map(indexedPropertyInfo -> {
					final String imageUrl = (String) source.getInputDocument().getFieldValue(indexedPropertyInfo.getTranslatedFieldName());
					return StringUtils.join(source.getMerchProductDirectoryConfigModel().getBaseImageUrl(), imageUrl);
				})
				.findFirst();
	}

}
