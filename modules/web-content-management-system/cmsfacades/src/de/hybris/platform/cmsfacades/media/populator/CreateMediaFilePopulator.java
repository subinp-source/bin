/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.media.populator;

import de.hybris.platform.cmsfacades.dto.MediaFileDto;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * This populator will populate the {@link MediaModel} with the information provided by the {@link MediaFileDto}
 */
public class CreateMediaFilePopulator implements Populator<MediaFileDto, MediaModel>
{
	@Override
	public void populate(final MediaFileDto source, final MediaModel target) throws ConversionException
	{
		target.setMime(source.getMime());
		target.setRealFileName(source.getName());
		target.setSize(source.getSize());
	}

}
