/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsoccaddon.mapping.converters;

import de.hybris.platform.cmsfacades.data.MediaData;
import de.hybris.platform.cmsoccaddon.data.MediaWsDTO;

/**
 * The converter to convert {@link MediaData} data object to {@link MediaWsDTO} ws object.
 */
public class MediaDataToWsConverter extends AbstractDataToWsConverter<MediaData, MediaWsDTO>
{
	@Override
	public Class getDataClass()
	{
		return MediaData.class;
	}

	@Override
	public Class getWsClass()
	{
		return MediaWsDTO.class;
	}
}
