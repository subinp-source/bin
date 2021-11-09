/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorfacades.device.impl;


import de.hybris.platform.acceleratorfacades.device.ResponsiveMediaFacade;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link ResponsiveMediaFacade}
 */
public class DefaultResponsiveMediaFacade implements ResponsiveMediaFacade
{
	private Converter<MediaContainerModel, List<ImageData>> mediaContainerConverter;

	@Override
	public List<ImageData> getImagesFromMediaContainer(final MediaContainerModel mediaContainerModel)
	{
		return getMediaContainerConverter().convert(mediaContainerModel);
	}

	public Converter<MediaContainerModel, List<ImageData>> getMediaContainerConverter()
	{
		return mediaContainerConverter;
	}

	@Required
	public void setMediaContainerConverter(final Converter<MediaContainerModel, List<ImageData>> mediaContainerConverter)
	{
		this.mediaContainerConverter = mediaContainerConverter;
	}
}
