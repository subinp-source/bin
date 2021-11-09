/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.product.converters.populator;

import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;

import org.springframework.util.Assert;


/**
 * Converter implementation for {@link de.hybris.platform.core.model.media.MediaModel} as source and {@link de.hybris.platform.commercefacades.product.data.ImageData} as target type.
 */
public class ImagePopulator implements Populator<MediaModel, ImageData>
{

	@Override
	public void populate(final MediaModel source, final ImageData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setUrl(source.getURL());
		target.setAltText(source.getAltText());
		if (source.getMediaFormat() != null)
		{
			target.setFormat(source.getMediaFormat().getQualifier());
		}
	}
}
